package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*

import scala.sys.process.Process

import zio.ZLayer
import zio.http.{Body, MediaType, Request}
import zio.test.{assertTrue, TestAspect, ZIOSpecDefault}

import Serializer.given
import Validator.*

object IntegrationTest extends ZIOSpecDefault:
  Process("psql -d poolbalance -f ddl.sql").run().exitValue()

  val conf = Resources.loadConfig("test.conf")
  val host = conf.getString("server.host")
  val port = conf.getInt("server.port")
  val endpoint = conf.getString("server.endpoint")
  val ds = conf.getConfig("ds")
  val email = conf.getConfig("email")
  val url = s"http://$host:$port$endpoint"
  println(s"*** Server url: $url")

  val emailAddress = "funkwerks@runbox.com"
  var account = Account()
  var pool = Pool(id = 0, name = "home", volume = 10_000, unit = UnitOfMeasure.gl.toString)
  var cleaning = Cleaning(poolId = 0)
  var measurement = Measurement(poolId = 0)
  var chemical = Chemical(poolId = 0)

  def spec = suite("server")(
    test("register > registered") {
      for
        registered <- register
      yield assertTrue(registered.isSuccess)
    },
    test("login > loggedIn") {
      for
        loggedIn <- login
      yield assertTrue(loggedIn.isSuccess)
    },
    test("add pool > pool added") {
      pool = pool.copy(license = account.license)
      for
        poolAdded <- addPool
      yield
        assertTrue(poolAdded.isSuccess)
    },
    test("update pool > pool updated") {
      pool = pool.copy(volume = 9_000)
      for
        poolUpdated <- updatePool
      yield assertTrue(poolUpdated.isSuccess)
    },
    test("list pools > pools listed") {
      for
        poolsListed <- listPools
      yield assertTrue(poolsListed.isSuccess)
    },
    test("add cleaning > cleaning added") {
      cleaning = cleaning.copy(poolId = pool.id)
      for
        cleaningAdded <- addCleaning
      yield assertTrue(cleaningAdded.isSuccess)
    },
    test("update cleaning > cleaning updated") {
      cleaning = cleaning.copy(vacuum = true)
      for
        cleaningUpdated <- updateCleaning
      yield assertTrue(cleaningUpdated.isSuccess)
    },
    test("list cleanings > cleanings listed") {
      for
        cleaningsListed <- listCleanings
      yield assertTrue(cleaningsListed.isSuccess)
    },
    test("add measurement > measurement added") {
      measurement = measurement.copy(poolId = pool.id)
      for
        measurementAdded <- addMeasurement
      yield assertTrue(measurementAdded.isSuccess)
    },
    test("update measurement > measurement updated") {
      measurement = measurement.copy(temperature = 90)
      for
        measurementUpdated <- updateMeasurement
      yield assertTrue(measurementUpdated.isSuccess)
    },
    test("list measurements > measurements listed") {
      for
        measurementsListed <- listMeasurements
      yield assertTrue(measurementsListed.isSuccess)
    },
    test("add chemical > chemical added") {
      chemical = chemical.copy(poolId = pool.id)
      for
        chemicalAdded <- addChemical
      yield assertTrue(chemicalAdded.isSuccess)
    },
    test("update chemical > chemical updated") {
      chemical = chemical.copy(amount = 2.0)
      for
        chemicalUpdated <- updateChemical
      yield assertTrue(chemicalUpdated.isSuccess)
    },
    test("list chemicals > chemicals listed") {
      for
        chemicalsListed <- listChemicals
      yield assertTrue(chemicalsListed.isSuccess)
    }
  ).provide(
    Store.dataSourceLayer(ds),
    Store.namingStrategyLayer,
    Store.licenseCacheLayer,
    Store.layer,
    Emailer.layer(email),
    Handler.layer
  ) @@ TestAspect.sequential

  def register =
    val command = writeToString[Command](Register(emailAddress))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield
      account = event.asInstanceOf[Registered].account
      assertTrue(account.isActivated)

  def login =
    val command = writeToString[Command](Login(account.emailAddress, account.pin))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[LoggedIn].account.isActivated)

  def addPool =
    val command = writeToString[Command](SavePool(account.license, pool))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield
      val poolSaved = event.asInstanceOf[PoolSaved]
      pool = pool.copy(id = poolSaved.id)
      assertTrue(poolSaved.id == 1L)

  def updatePool =
    val command = writeToString[Command](SavePool(account.license, pool))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[PoolSaved].id == 1L)

  def listPools =
    val command = writeToString[Command](ListPools(account.license))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[PoolsListed].pools.length == 1)

  def addCleaning =
    val command = writeToString[Command](SaveCleaning(account.license, cleaning))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield
      val cleaningSaved = event.asInstanceOf[CleaningSaved]
      cleaning = cleaning.copy(id = cleaningSaved.id)
      assertTrue(cleaningSaved.id == 1L)

  def updateCleaning =
    val command = writeToString[Command](SaveCleaning(account.license, cleaning))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[CleaningSaved].id == 1L)

  def listCleanings =
    val command = writeToString[Command](ListCleanings(account.license, pool.id))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[CleaningsListed].cleanings.length == 1)

  def addMeasurement =
    val command = writeToString[Command](SaveMeasurement(account.license, measurement))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield
      val measurementSaved = event.asInstanceOf[MeasurementSaved]
      measurement = measurement.copy(id = measurementSaved.id)
      assertTrue(measurementSaved.id == 1L)

  def updateMeasurement =
    val command = writeToString[Command](SaveMeasurement(account.license, measurement))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[MeasurementSaved].id == 1L)

  def listMeasurements =
    val command = writeToString[Command](ListMeasurements(account.license, pool.id))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[MeasurementsListed].measurements.length == 1)

  def addChemical =
    val command = writeToString[Command](SaveChemical(account.license, chemical))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield
      val chemicalSaved = event.asInstanceOf[ChemicalSaved]
      chemical = chemical.copy(id = chemicalSaved.id)
      assertTrue(chemicalSaved.id == 1L)

  def updateChemical =
    val command = writeToString[Command](SaveChemical(account.license, chemical))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[ChemicalSaved].id == 1L)

  def listChemicals =
    val command = writeToString[Command](ListChemicals(account.license, pool.id))
    val request = Request.post(url, Body.fromString(command).contentType(MediaType.application.json))
    for
      response <- Server.routes.runZIO(request)
      json     <- response.body.asString
      event    =  readFromString[Event](json)
    yield assertTrue(event.asInstanceOf[ChemicalsListed].chemicals.length == 1)