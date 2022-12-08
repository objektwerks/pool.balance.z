package objektwerks

import scala.sys.process.Process

import zio.{Console, Scope, ZIO}
import zio.http.{Body, Client, ClientConfig}
import zio.json.{DecoderOps, EncoderOps}
import zio.test.{assertTrue, ZIOSpecDefault}
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
    test("run") {
      for
        registered <- register
        loggedIn   <- login
      yield
        assertTrue(registered.isSuccess) &&
        assertTrue(loggedIn.isSuccess)
    }
  ).provide(Client.default, Scope.default)

  val register =
    for
      response <- Client.request(url = url, content = Body.fromString(Register().toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[Event] match
                      case Right(event) => event match
                        case registered @ Registered(_) =>
                          this.account = registered.account
                          assertTrue(account.isActivated)
                        case _ =>
                          Console.printLine("Register > Registered failed!") *> assertTrue(false)
                      case Left(error) =>
                        Console.printLine(s"Register > Registered failed: $error") *> assertTrue(false)
                    }
    yield result

  val login =
    for
      response <- Client.request(url = url, content = Body.fromString(Login(account.pin).toJson))
      result   <- response.body.asString.flatMap { json =>
                    json.fromJson[Event] match
                      case Right(event) => event match
                        case LoggedIn(account) =>
                          assertTrue(account.isActivated)
                        case _ =>
                          Console.printLine("Login > LoggedIn failed!") *> assertTrue(false)
                      case Left(error) =>
                        Console.printLine(s"Login > LoggedIn failed: $error") *> assertTrue(false)
                    }
    yield result