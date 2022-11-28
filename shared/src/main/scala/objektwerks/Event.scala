package objektwerks

sealed trait Event

final case class PoolsListed() extends Event
final case class PoolSaved(pool: Pool, isNew: Boolean) extends Event

final case class CleaningsListed() extends Event
final case class CleaningSaved(cleaning: Cleaning, isNew: Boolean) extends Event

final case class MeasurementsListed() extends Event
final case class MeasurementSaved(measurement: Measurement, isNew: Boolean) extends Event

final case class ChemicalsListed() extends Event
final case class ChemicalSaved(chemical: Chemical, isNew: Boolean) extends Event
