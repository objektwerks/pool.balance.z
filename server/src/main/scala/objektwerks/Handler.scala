package objektwerks

import zio.{Task, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given
import Validator.*

final case class Handler(store: Store, emailer: Emailer):
  def handle[E <: Event](command: Command): Task[Event] =
    for
      isAuthorized <- authorize(command)
      isValid      <- validate(command)
      event        <- if isAuthorized && isValid then command match
                      case Register(emailAddress)          => register(emailAddress)
                      case Login(emailAddress, pin)        => login(emailAddress, pin)
                      case Deactivate(license)             => deactivateAccount(license)
                      case Reactivate(license)             => reactivateAccount(license)
                      case ListPools(license)              => listPools(license)
                      case SavePool(_, pool)               => savePool(pool)
                      case ListCleanings(_, poolId)        => listCleanings(poolId)
                      case SaveCleaning(_, cleaning)       => saveCleaning(cleaning)
                      case ListMeasurements(_, poolId)     => listMeasurements(poolId)
                      case SaveMeasurement(_, measurement) => saveMeasurement(measurement)
                      case ListChemicals(_, poolId)        => listChemicals(poolId)
                      case SaveChemical(_, chemical)       => saveChemical(chemical)
                      else ZIO.succeed( Fault(s"Invalid command: $command") )
    yield event

  private val subject = "Account Registration"

  private def authorize(command: Command): Task[Boolean] =
    command match
      case license: License          => if license.isLicense then store.authorize(license.license) else ZIO.succeed(false)
      case Register(_) | Login(_, _) => ZIO.succeed(true)

  private def validate(command: Command): Task[Boolean] = ZIO.succeed(command.isValid)

  private def register(emailAddress: String): Task[Registered | Fault] =
    val account = Account(emailAddress = emailAddress)
    val sent = email(account.emailAddress, account.pin)
    if sent then
      for
        id    <- store.register(account)
      yield Registered( account.copy(id = id) )
    else ZIO.succeed( Fault(s"Registration failed for: $emailAddress") )

  private def email(emailAddress: String, pin: String): Boolean =
    val recipients = List(emailAddress)
    val message = s"<p>Save this pin: <b>${pin}</b> in a safe place; then delete this email.</p>"
    emailer.send(recipients, subject, message)

  private def login(emailAddress: String, pin: String): Task[LoggedIn | Fault] =
    for
      option <- store.login(emailAddress, pin)
    yield if option.isDefined then LoggedIn(option.get) else Fault(s"Invalid pin: $pin")

  private def deactivateAccount(license: String): Task[Deactivated] =
    for
      account <- store.deactivateAccount(license)
    yield Deactivated(account)

  private def reactivateAccount(license: String): Task[Reactivated] =
    for
      account <- store.reactivateAccount(license)
    yield Reactivated(account)

  private def listPools(license: String): Task[PoolsListed] =
    for
      pools <- store.listPools(license)
    yield PoolsListed(pools)

  private def savePool(pool: Pool): Task[PoolSaved] =
    for
      id <- if pool.id == 0 then store.addPool(pool) else store.updatePool(pool)
    yield PoolSaved(id)

  private def listCleanings(poolId: Long): Task[CleaningsListed] =
    for
      cleanings <- store.listCleanings(poolId)
    yield CleaningsListed(cleanings)

  private def saveCleaning(cleaning: Cleaning): Task[CleaningSaved] =
    for
      id <- if cleaning.id == 0 then store.addCleaning(cleaning) else store.updateCleaning(cleaning)
    yield CleaningSaved(id)

  private def listMeasurements(poolId: Long): Task[MeasurementsListed] =
    for
      measurements <- store.listMeasurements(poolId)
    yield MeasurementsListed(measurements)

  private def saveMeasurement(measurement: Measurement): Task[MeasurementSaved] =
    for
      id <- if measurement.id == 0 then store.addMeasurement(measurement) else store.updateMeasurement(measurement)
    yield MeasurementSaved(id)

  private def listChemicals(poolId: Long): Task[ChemicalsListed] =
    for
      chemicals <- store.listChemicals(poolId)
    yield ChemicalsListed(chemicals)

  private def saveChemical(chemical: Chemical): Task[ChemicalSaved] =
    for
      id <- if chemical.id == 0 then store.addChemical(chemical) else store.updateChemical(chemical)
    yield ChemicalSaved(id)

object Handler:
  val layer: ZLayer[Store & Emailer, Nothing, Handler] =
    ZLayer {
      for
        store   <- ZIO.service[Store]
        emailer <- ZIO.service[Emailer]
      yield Handler(store, emailer)
    }
