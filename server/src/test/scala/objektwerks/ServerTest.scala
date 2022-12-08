package objektwerks

import scala.annotation.nowarn
import scala.sys.process.Process

import zio.ZIO
import zio.http.{!!, /, ->, Body, Http, Request, Response, URL}
import zio.http.model.Method
import zio.test.{assertTrue, ZIOSpecDefault}

object ServerTest extends ZIOSpecDefault:
  @nowarn Process("psql -d poolmate -f ddl.sql").run().exitValue()

  val router = Router.router

  def spec = suite("server")(
    test("run") {
      assertTrue(true)
    }
  )