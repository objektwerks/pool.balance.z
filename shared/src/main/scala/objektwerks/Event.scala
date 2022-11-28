package objektwerks

sealed trait Event

final case class PoolsListed() extends Event
final case class PoolAdded(pool: Pool) extends Event
final case class PoolUpdated(pool: Pool) extends Event

final case class CleaningsListed() extends Event
final case class CleaningAdded(cleaning: Cleaning) extends Event
final case class CleaningUpdated(cleaning: Cleaning) extends Event

final case class MeasurementsListed() extends Event
final case class MeasurementAdded(measurement: Measurement) extends Event
final case class MeasurementUpdated(measurement: Measurement) extends Event

final case class ChemicalsListed() extends Event
final case class ChemicalAdded(chemical: Chemical) extends Event
final case class ChemicalUpdated(chemical: Chemical) extends Event