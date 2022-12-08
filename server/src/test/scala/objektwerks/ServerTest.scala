package objektwerks

import scala.sys.process.Process

import zio.ZIO
import zio.http.{!!, /, ->, Body, Http, Request, Response, URL}
import zio.http.model.Method
import zio.test.{assertTrue, ZIOSpecDefault}

object ServerTest extends ZIOSpecDefault:
  val exitCode = Process("psql -d poolmate -f ddl.sql").run().exitValue()

  def spec = suite("server")(
    test("run") {
      assertTrue(true)
    }
  )