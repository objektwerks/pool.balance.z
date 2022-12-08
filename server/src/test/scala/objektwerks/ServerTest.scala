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

object ServerTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolbalance -f ddl.sql").run().exitValue()
  val server = Server.run

  val conf = Resources.loadConfig("server.conf")
  val host = conf.getString("host")
  val port = conf.getInt("port")
  val url = s"http://$host:$port/command"

  var account = Account()

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