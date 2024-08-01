package objektwerks

sealed trait Command

sealed trait License:
  val license: String

final case class Register(emailAddress: String) extends Command derives CanEqual
final case class Login(emailAddress: String, pin: String) extends Command derives CanEqual

final case class Deactivate(license: String) extends Command with License derives CanEqual
final case class Reactivate(license: String) extends Command with License derives CanEqual

final case class ListPools(license: String) extends Command with License derives CanEqual
final case class SavePool(license: String, pool: Pool) extends Command with License derives CanEqual

final case class ListCleanings(license: String, poolId: Long) extends Command with License derives CanEqual
final case class SaveCleaning(license: String, cleaning: Cleaning) extends Command with License derives CanEqual

final case class ListMeasurements(license: String, poolId: Long) extends Command with License derives CanEqual
final case class SaveMeasurement(license: String, measurement: Measurement) extends Command with License derives CanEqual

final case class ListChemicals(license: String, poolId: Long) extends Command with License derives CanEqual
final case class SaveChemical(license: String, chemical: Chemical) extends Command with License derives CanEqual