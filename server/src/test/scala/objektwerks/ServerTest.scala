package objektwerks

import scala.sys.process.Process

import zio.ZIO
import zio.test.{assertTrue, ZIOSpecDefault}

object ServerTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolmate -f ddl.sql").run().exitValue()
  
  def spec = suite("server")(
    test("run") {
      val server = Server.run
      assertTrue(true)
    }
  )