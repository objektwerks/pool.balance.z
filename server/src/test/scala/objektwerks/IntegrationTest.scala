package objektwerks

import scala.sys.process.Process

import zio.{Console, Scope, ZIO}
import zio.http.{Body, Client}
import zio.json.{DecoderOps, EncoderOps}
import zio.test.{assertTrue, TestAspect, ZIOSpecDefault}
import zio.test.Assertion.isSuccess

import Serializer.given
import Validator.given
import Validator.*

object IntegrationTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolbalance -f ddl.sql").run().exitValue()
  val router = Router.run

  val conf = Resources.loadConfig("server.conf")
  val host = conf.getString("host")
  val port = conf.getInt("port")
  val url = s"http://$host:$port/command"

  var account = Account()
  var pool = Pool(id = 0, name = "home", volume = 10_000, unit = UnitOfMeasure.gl.toString)

  def spec = suite("server")(
    test("register > registered") {
      for
        registered <- register
      yield assertTrue(registered.isSuccess)
    },
    test("login > loggedIn") {
      for
        loggedIn   <- login
      yield assertTrue(loggedIn.isSuccess)
    },
    test("add pool > pool added") {
      for
        poolAdded   <- addPool
      yield assertTrue(poolAdded.isSuccess)
    },
    test("update pool > pool updated") {
      pool = pool.copy(volume = 9_000)
      for
        poolUpdated   <- updatePool
      yield assertTrue(poolUpdated.isSuccess)
    },
    test("list pools > pools listed") {
      for
        poolsListed   <- listPools
      yield assertTrue(poolsListed.isSuccess)
    }
  ).provide(Client.default, Scope.default) @@ TestAspect.sequential

  val register =
    for
      response <- Client.request(url = url, content = Body.fromString(Register().toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[Registered] match
                      case Right(registered) => this.account = registered.account; assertTrue(account.isActivated)
                      case Left(error) => Console.printLine(s"Register > Registered failed: $error") *> assertTrue(false)
                  }
    yield result

  val login =
    for
      response <- Client.request(url = url, content = Body.fromString(Login(account.pin).toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[LoggedIn] match
                      case Right(loggedIn) => assertTrue(loggedIn.account.isActivated)
                      case Left(error) => Console.printLine(s"Login > LoggedIn failed: $error") *> assertTrue(false)
                  }
    yield result

  val addPool =
    for
      response <- Client.request(url = url, content = Body.fromString(SavePool(account.license, pool).toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[PoolSaved] match
                      case Right(poolSaved) => pool = pool.copy(id = poolSaved.id); assertTrue(poolSaved.id == 1L)
                      case Left(error) => Console.printLine(s"SavePool > PoolSaved ( add ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val updatePool =
    for
      response <- Client.request(url = url, content = Body.fromString(SavePool(account.license, pool).toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[PoolSaved] match
                      case Right(poolSaved) => assertTrue(poolSaved.id == 1L)
                      case Left(error) => Console.printLine(s"SavePool > PoolSaved ( update ) failed: $error") *> assertTrue(false)
                  }
    yield result

  val listPools =
    for
      response <- Client.request(url = url, content = Body.fromString(ListPools(account.license).toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[PoolsListed] match
                      case Right(poolsListed) => assertTrue(poolsListed.pools.length == 1)
                      case Left(error) => Console.printLine(s"SavePool > PoolSaved failed: $error") *> assertTrue(false)
                  }
    yield result

  val addCleaning = ???
  val updateCleaning = ???
  val listCleanings = ???

  val addMeasurement = ???
  val updateMeasurement = ???
  val listMeasurements = ???

  