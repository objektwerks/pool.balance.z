package objektwerks

sealed trait Command

sealed trait License:
  val license: String

final case class Register(emailAddress: String) extends Command
final case class Login(emailAddress: String, pin: String) extends Command

final case class Deactivate(license: String) extends Command with License
final case class Reactivate(license: String) extends Command with License

final case class ListPools() extends Command
final case class SavePool(pool: Pool) extends Command

final case class ListCleanings() extends Command
final case class SaveCleaning(cleaning: Cleaning) extends Command

final case class ListMeasurements() extends Command
final case class SaveMeasurement(measurement: Measurement) extends Command

final case class ListChemicals() extends Command
final case class SaveChemical(chemical: Chemical) extends Command