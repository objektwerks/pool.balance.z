package objektwerks

import scala.sys.process.Process

import zio.{Console, ZLayer}
import zio.http.{Body, Request}
import zio.json.{DecoderOps, EncoderOps}
import zio.test.{assertTrue, TestAspect, ZIOSpecDefault}

import Serializer.given
import Validator.*

object IntegrationTest extends ZIOSpecDefault:
  val conf = Resources.loadConfig("test.conf")
  val host = conf.getString("server.host")
  val port = conf.getInt("server.port")
  val ds = conf.getConfig("ds")
  val email = conf.getConfig("email")
  val config = zio.http.Server.Config.default.binding(host, port)
  val url = s"http://$host:$port/command"
  println(s"*** Server url: $url")

  val emailAddress = "funkwerks@runbox.com"
  var account = Account()
  var pool = Pool(id = 0, name = "home", volume = 10_000, unit = UnitOfMeasure.gl.toString)
  var cleaning = Cleaning(poolId = 0)
  var measurement = Measurement(poolId = 0)
  var chemical = Chemical(poolId = 0)

  Process("psql -d poolbalance -f ddl.sql").run().exitValue()

  def spec = suite("server")(
    test("register > registered") {
      for
        registered <- register
      yield assertTrue(registered.isSuccess)
    },
    test("login > loggedIn") {
      for
        loggedIn <- login
      yield assertTrue(loggedIn.isSuccess)
    },
    test("add pool > pool added") {
      pool = pool.copy(license = account.license)
      for
        poolAdded <- addPool
      yield assertTrue(poolAdded.isSuccess)
    },
    test("update pool > pool updated") {
      pool = pool.copy(volume = 9_000)
      for
        poolUpdated <- updatePool
      yield assertTrue(poolUpdated.isSuccess)
    },
    test("list pools > pools listed") {
      for
        poolsListed <- listPools
      yield assertTrue(poolsListed.isSuccess)
    },
    test("add cleaning > cleaning added") {
      cleaning = cleaning.copy(poolId = pool.id)
      for
        cleaningAdded <- addCleaning
      yield assertTrue(cleaningAdded.isSuccess)
    },
    test("update cleaning > cleaning updated") {
      cleaning = cleaning.copy(vacuum = true)
      for
        cleaningUpdated <- updateCleaning
      yield assertTrue(cleaningUpdated.isSuccess)
    },
    test("list cleanings > cleanings listed") {
      for
        cleaningsListed <- listCleanings
      yield assertTrue(cleaningsListed.isSuccess)
    },
    test("add measurement > measurement added") {
      measurement = measurement.copy(poolId = pool.id)
      for
        measurementAdded <- addMeasurement
      yield assertTrue(measurementAdded.isSuccess)
    },
    test("update measurement > measurement updated") {
      measurement = measurement.copy(temperature = 90)
      for
        measurementUpdated <- updateMeasurement
      yield assertTrue(measurementUpdated.isSuccess)
    },
    test("list measurements > measurements listed") {
      for
        measurementsListed <- listMeasurements
      yield assertTrue(measurementsListed.isSuccess)
    },
    test("add chemical > chemical added") {
      chemical = chemical.copy(poolId = pool.id)
      for
        chemicalAdded <- addChemical
      yield assertTrue(chemicalAdded.isSuccess)
    },
    test("update chemical > chemical updated") {
      chemical = chemical.copy(amount = 2.0)
      for
        chemicalUpdated <- updateChemical
      yield assertTrue(chemicalUpdated.isSuccess)
    },
    test("list chemicals > chemicals listed") {
      for
        chemicalsListed <- listChemicals
      yield assertTrue(chemicalsListed.isSuccess)
    }
  ).provide(
    Store.dataSourceLayer(ds),
    Store.namingStrategyLayer,
    Store.licenseCacheLayer,
    Store.layer,
    Emailer.layer(email),
    Handler.layer
  ) @@ TestAspect.sequential

  val register =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(Register(emailAddress).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[Registered] match
                      case Right(registered) =>
                        this.account = registered.account
                        assertTrue(account.isActivated)
                      case Left(error) => Console.printLine(s"Register > Registered failed: $error") *> assertTrue(false)
                  }
    yield result

  val login =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(Login(account.emailAddress, account.pin).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[LoggedIn] match
                      case Right(loggedIn) => assertTrue(loggedIn.account.isActivated)
                      case Left(error) => Console.printLine(s"Login > LoggedIn failed: $error") *> assertTrue(false)
                  }
    yield result

  val addPool =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SavePool(account.license, pool).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[PoolSaved] match
                      case Right(added) =>
                        pool = pool.copy(id = added.id)
                        assertTrue(added.id == 1L)
                      case Left(error) => Console.printLine(s"SavePool > PoolSaved ( add ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val updatePool =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SavePool(account.license, pool).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[PoolSaved] match
                      case Right(updated) => assertTrue(updated.id == 1L)
                      case Left(error) => Console.printLine(s"SavePool > PoolSaved ( update ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val listPools =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(ListPools(account.license).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[PoolsListed] match
                      case Right(list) => assertTrue(list.pools.length == 1)
                      case Left(error) => Console.printLine(s"ListPools > PoolsListed failed: $error") *> assertTrue(false)
                  }
    yield result

  val addCleaning =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveCleaning(account.license, cleaning).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[CleaningSaved] match
                      case Right(added) =>
                        cleaning = cleaning.copy(id = added.id)
                        assertTrue(added.id == 1L)
                      case Left(error) => Console.printLine(s"SaveCleaning > CleaningSaved ( add ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val updateCleaning =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveCleaning(account.license, cleaning).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[CleaningSaved] match
                      case Right(updated) => assertTrue(updated.id == 1L)
                      case Left(error) => Console.printLine(s"SaveCleaning > CleaningSaved ( update ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val listCleanings =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(ListCleanings(account.license, pool.id).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[CleaningsListed] match
                      case Right(list) => assertTrue(list.cleanings.length == 1)
                      case Left(error) => Console.printLine(s"ListCleanings > CleaningsListed failed: $error") *> assertTrue(false)
                  }
    yield result

  val addMeasurement =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveMeasurement(account.license, measurement).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[MeasurementSaved] match
                      case Right(added) =>
                        measurement = measurement.copy(id = added.id)
                        assertTrue(added.id == 1L)
                      case Left(error) => Console.printLine(s"SaveMeasurement > MeasurementSaved ( add ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val updateMeasurement =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveMeasurement(account.license, measurement).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[MeasurementSaved] match
                      case Right(updated) => assertTrue(updated.id == 1L)
                      case Left(error) => Console.printLine(s"SaveMeasurement > MeasurementSaved ( update ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val listMeasurements =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(ListMeasurements(account.license, pool.id).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[MeasurementsListed] match
                      case Right(list) => assertTrue(list.measurements.length == 1)
                      case Left(error) => Console.printLine(s"ListMeasurements > MeasurementsListed failed: $error") *> assertTrue(false)
                  }
    yield result

  val addChemical =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveChemical(account.license, chemical).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[ChemicalSaved] match
                      case Right(added) =>
                        chemical = chemical.copy(id = added.id)
                        assertTrue(added.id == 1L)
                      case Left(error) => Console.printLine(s"SavePool > PoolSaved ( add ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val updateChemical =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveChemical(account.license, chemical).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[ChemicalSaved] match
                      case Right(updated) => assertTrue(updated.id == 1L)
                      case Left(error) => Console.printLine(s"SaveChemical > ChemicalSaved ( update ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val listChemicals =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(ListChemicals(account.license, pool.id).toJson)) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[ChemicalsListed] match
                      case Right(list) => assertTrue(list.chemicals.length == 1)
                      case Left(error) => Console.printLine(s"ListChemicals > ChemicalsListed failed: $error") *> assertTrue(false)
                  }
    yield result