package objektwerks

import zio.{Task, ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given
import Validator.*

final case class Handler(store: Store):
  def handle[E <: Event](command: Command): Task[Event] =
    for
      isAuthorized <- authorize(command)
      isValid      <- validate(command)
      event        <- if isAuthorized && isValid then command match
                      case Register(emailAddress)          => register(emailAddress)
                      case Login(pin)                      => login(pin)
                      case Deactivate(license)             => deactivateAccount(license)
                      case Reactivate(license)             => reactivateAccount(license)
                      case ListPools(_)                    => listPools
                      case SavePool(_, pool)               => savePool(pool)
                      case ListCleanings(_)                => listCleanings
                      case SaveCleaning(_, cleaning)       => saveCleaning(cleaning)
                      case ListMeasurements(_)             => listMeasurements
                      case SaveMeasurement(_, measurement) => saveMeasurement(measurement)
                      case ListChemicals(_)                => listChemicals
                      case SaveChemical(_, chemical)       => saveChemical(chemical)
                      else ZIO.succeed( Fault(s"Invalid command: $command") )
    yield event

  private def authorize(command: Command): Task[Boolean] =
    command match
      case license: License       => if license.isLicense then store.authorize(license.license) else ZIO.succeed(false)
      case Register(_) | Login(_) => ZIO.succeed(true)

  private def validate(command: Command): Task[Boolean] = ZIO.succeed(command.isValid)

  private def register(emailAddress: String): Task[Registered] =
    val account = Account()
    for
      id <- store.register(account)
    yield Registered( account.copy(id = id) )

  private def login(pin: String): Task[LoggedIn | Fault] =
    for
      option <- store.login(pin)
    yield if option.isDefined then LoggedIn(option.get) else Fault(s"Invalid pin: $pin")

  private def deactivateAccount(license: String): Task[Deactivated] =
    for
      account <- store.deactivateAccount(license)
    yield Deactivated(account)

  private def reactivateAccount(license: String): Task[Reactivated] =
    for
      account <- store.reactivateAccount(license)
    yield Reactivated(account)

  private def listPools: Task[PoolsListed] =
    for
      pools <- store.listPools
    yield PoolsListed(pools)

  private def savePool(pool: Pool): Task[PoolSaved] =
    for
      id <- if pool.id == 0 then store.addPool(pool) else store.updatePool(pool)
    yield PoolSaved(id)

  private def listCleanings: Task[CleaningsListed] =
    for
      cleanings <- store.listCleanings
    yield CleaningsListed(cleanings)

  private def saveCleaning(cleaning: Cleaning): Task[CleaningSaved] =
    for
      id <- if cleaning.id == 0 then store.addCleaning(cleaning) else store.updateCleaning(cleaning)
    yield CleaningSaved(id)

  private def listMeasurements: Task[MeasurementsListed] =
    for
      measurements <- store.listMeasurements
    yield MeasurementsListed(measurements)

  private def saveMeasurement(measurement: Measurement): Task[MeasurementSaved] =
    for
      id <- if measurement.id == 0 then store.addMeasurement(measurement) else store.updateMeasurement(measurement)
    yield MeasurementSaved(id)

  private def listChemicals: Task[ChemicalsListed] =
    for
      chemicals <- store.listChemicals
    yield ChemicalsListed(chemicals)

  private def saveChemical(chemical: Chemical): Task[ChemicalSaved] =
    for
      id <- if chemical.id == 0 then store.addChemical(chemical) else store.updateChemical(chemical)
    yield ChemicalSaved(id)

object Handler:
  val layer: ZLayer[Store, Nothing, Handler] =
    ZLayer {
      for
        store <- ZIO.service[Store]
      yield Handler(store)
    }