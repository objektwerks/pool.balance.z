package objektwerks

sealed trait Command

final case class ListPools() extends Command
final case class SavePool(pool: Pool, isNew: Boolean) extends Command

final case class ListCleanings() extends Command
final case class SaveCleaning(cleaning: Cleaning, isNew: Boolean) extends Command

final case class ListMeasurements() extends Command
final case class SaveMeasurement(measurement: Measurement, isNew: Boolean) extends Command

final case class ListChemicals() extends Command
final case class SaveChemical(chemical: Chemical, isNew: Boolean) extends Command
