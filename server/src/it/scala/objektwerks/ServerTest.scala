package objektwerks

import zio.ZIO
import zio.http.{!!, /, ->, Body, Http, Request, Response, URL}
import zio.http.model.Method
import zio.test.{assertTrue, ZIOSpecDefault}

object ServerTest extends ZIOSpecDefault:
  def spec = suite("server")(
    test("run") {
      val request = Request.get(URL(!! / "get"))
      for
        response <- route(request).flatMap(response => response.body.asString)
      yield assertTrue(response == "get")
    }
  )