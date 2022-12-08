package objektwerks

import scala.sys.process.Process

import zio.{Console, ZIO}
import zio.http.Client
import zio.json.{DecoderOps, EncoderOps}
import zio.test.{assertTrue, ZIOSpecDefault}

import Serializer.given
import Validator.given
import Validator.*

object ServerTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolbalance -f ddl.sql").run().exitValue()
  val server = Server.run
  val url = "http://localhost:7272/command"

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

  def spec = suite("server")(
    test("run") {
      assertTrue(true)
    }
  )