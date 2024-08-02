package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*

import scala.sys.process.Process

import zio.{Console, ZLayer}
import zio.http.{Body, Request}
import zio.json.{DecoderOps, EncoderOps}
import zio.test.{assertTrue, TestAspect, ZIOSpecDefault}

import Serializer.given
import Validator.*

object IntegrationTest extends ZIOSpecDefault:
  Process("psql -d poolbalance -f ddl.sql").run().exitValue()

  val conf = Resources.loadConfig("test.conf")
  val host = conf.getString("server.host")
  val port = conf.getInt("server.port")
  val endpoint = conf.getString("server.endpoint")
  val ds = conf.getConfig("ds")
  val email = conf.getConfig("email")
  val url = s"http://$host:$port$endpoint"
  println(s"*** Server url: $url")

  val emailAddress = "funkwerks@runbox.com"
  var account = Account()
  var pool = Pool(id = 0, name = "home", volume = 10_000, unit = UnitOfMeasure.gl.toString)
  var cleaning = Cleaning(poolId = 0)
  var measurement = Measurement(poolId = 0)
  var chemical = Chemical(poolId = 0)

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

  def register =
    val register = writeToString[Register](Register(emailAddress))
    val request  = Request.post(url, Body.fromString(register))
    for
      response   <- Server.routes.runZIO(request)
      json       <- response.body.asString.orDie
      registered =  readFromString[Registered](json)
    yield
      account = registered.account
      assertTrue(account.isActivated)

  def login =
    val login   = writeToString[Login](Login(account.emailAddress, account.pin))
    val request = Request.post(url, Body.fromString(login))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString.orDie
      loggedIn =  readFromString[LoggedIn](json)
    yield assertTrue(loggedIn.account.isActivated)

  def addPool =
    val savePool = writeToString[SavePool](SavePool(account.license, pool))
    val request  = Request.post(url, Body.fromString(savePool))
    for
      response  <- Server.routes.runZIO(request)
      json      <- response.body.asString.orDie
      poolSaved =  readFromString[PoolSaved](json)
    yield
      pool = pool.copy(id = poolSaved.id)
      assertTrue(poolSaved.id == 1L)

  def updatePool =
    val savePool = writeToString[SavePool](SavePool(account.license, pool))
    val request  = Request.post(url, Body.fromString(savePool))
    for
      response  <- Server.routes.runZIO(request)
      json      <- response.body.asString.orDie
      poolSaved =  readFromString[PoolSaved](json)
    yield assertTrue(poolSaved.id == 1L)

  def listPools =
    val listPools = writeToString[ListPools](ListPools(account.license))
    val request   = Request.post(url, Body.fromString(listPools))
    for
      response    <- Server.routes.runZIO(request)
      json        <- response.body.asString.orDie
      poolsListed =  readFromString[PoolsListed](json)
    yield assertTrue(poolsListed.pools.length == 1)

  def addCleaning =
    val saveCleaning = writeToString[SaveCleaning](SaveCleaning(account.license, cleaning))
    val request      = Request.post(url, Body.fromString(saveCleaning))
    for
      response      <- Server.routes.runZIO(request)
      json          <- response.body.asString.orDie
      cleaningSaved =  readFromString[CleaningSaved](json)
    yield
      cleaning = cleaning.copy(id = cleaningSaved.id)
      assertTrue(cleaningSaved.id == 1L)

  def updateCleaning =
    val saveCleaning = writeToString[SaveCleaning](SaveCleaning(account.license, cleaning))
    val request      = Request.post(url, Body.fromString(saveCleaning))
    for
      response      <- Server.routes.runZIO(request)
      json          <- response.body.asString.orDie
      cleaningSaved =  readFromString[CleaningSaved](json)
    yield assertTrue(cleaningSaved.id == 1L)

  def listCleanings =
    val listCleanings = writeToString[ListCleanings](ListCleanings(account.license, pool.id))
    val request       = Request.post(url, Body.fromString(listCleanings))
    for
      response        <- Server.routes.runZIO(request)
      json            <- response.body.asString.orDie
      cleaningsListed =  readFromString[CleaningsListed](json)
    yield assertTrue(cleaningsListed.cleanings.length == 1)

  def addMeasurement =
    val saveMeasurement = writeToString[SaveMeasurement](SaveMeasurement(account.license, measurement))
    val request         = Request.post(url, Body.fromString(saveMeasurement))
    for
      response         <- Server.routes.runZIO(request)
      json             <- response.body.asString.orDie
      measurementSaved =  readFromString[MeasurementSaved](json)
    yield
      measurement = measurement.copy(id = measurementSaved.id)
      assertTrue(measurementSaved.id == 1L)

  def updateMeasurement =
    val saveMeasurement = writeToString[SaveMeasurement](SaveMeasurement(account.license, measurement))
    val request         = Request.post(url, Body.fromString(saveMeasurement))
    for
      response         <- Server.routes.runZIO(request)
      json             <- response.body.asString.orDie
      measurementSaved =  readFromString[MeasurementSaved](json)
    yield
      assertTrue(measurementSaved.id == 1L)

  def listMeasurements =
    val listMeasurements = writeToString[ListMeasurements](ListMeasurements(account.license, pool.id))
    val request          = Request.post(url, Body.fromString(listMeasurements))
    for
      response            <- Server.routes.runZIO(request)
      json                <- response.body.asString.orDie
      measurementsListed  =  readFromString[MeasurementsListed](json)
    yield assertTrue(measurementsListed.measurements.length == 1)

  def addChemical =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveChemical(account.license, chemical))) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[ChemicalSaved] match
                      case Right(added) =>
                        chemical = chemical.copy(id = added.id)
                        assertTrue(added.id == 1L)
                      case Left(error) => Console.printLine(s"*** SavePool > PoolSaved ( add ) failed: $error") *> assertTrue(false)
                  }
    yield result

  def updateChemical =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(SaveChemical(account.license, chemical))) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[ChemicalSaved] match
                      case Right(updated) => assertTrue(updated.id == 1L)
                      case Left(error) => Console.printLine(s"*** SaveChemical > ChemicalSaved ( update ) failed: $error") *> assertTrue(false)
                  }
    yield result

  def listChemicals =
    for
      response <- Server.routes.runZIO( Request.post(url, Body.fromString(ListChemicals(account.license, pool.id))) )
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[ChemicalsListed] match
                      case Right(list) => assertTrue(list.chemicals.length == 1)
                      case Left(error) => Console.printLine(s"*** ListChemicals > ChemicalsListed failed: $error") *> assertTrue(false)
                  }
    yield result