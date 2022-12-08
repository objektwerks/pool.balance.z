package objektwerks

import scala.sys.process.Process

import zio.{Console, Scope, ZIO}
import zio.http.{Client, ClientConfig}
import zio.http.netty.client.ConnectionPool
import zio.json.{DecoderOps, EncoderOps}
import zio.test.{assertTrue, assertZIO, ZIOSpecDefault}
import zio.test.Assertion.isSuccess

import Serializer.given
import Validator.given
import Validator.*


object ServerTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolbalance -f ddl.sql").run().exitValue()
  val server = Server.run
  val url = "http://localhost:7272/command"

  def spec = suite("server")(
    test("run") {
      for
        result <- register
      yield assertTrue(result.isSuccess)
    }
  ).provide(Client.default, Scope.default)

  val register =
    for {
      response <- Client.request(url)
      result   <- response.body.asString.flatMap { json => json.fromJson[Event] match
                    case Right(event) => event match
                      case Registered(account) => assertTrue(account.isActivated)
                      case _ => Console.printLine("Registered failed!") *> assertTrue(false)
                    case Left(error) => Console.printLine(s"Registered failed: $error") *> assertTrue(false)
                  }
    } yield result