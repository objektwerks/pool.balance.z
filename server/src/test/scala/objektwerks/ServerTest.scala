package objektwerks

import scala.sys.process.Process

import zio.ZIO
import zio.test.{assertTrue, ZIOSpecDefault}

object ServerTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolbalance -f ddl.sql").run().exitValue()
  val server = Server.run

  def spec = suite("server")(
    test("run") {
      assertTrue(true)
    }
  )