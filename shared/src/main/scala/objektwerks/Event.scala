package objektwerks

sealed trait Event

final case class PoolsListed(pools: List[Pool]) extends Event
final case class PoolSaved(pool: Pool, isNew: Boolean) extends Event

final case class CleaningsListed(cleanings: List[Cleaning]) extends Event
final case class CleaningSaved(cleaning: Cleaning, isNew: Boolean) extends Event

final case class MeasurementsListed(measurements: List[Measurement]) extends Event
final case class MeasurementSaved(measurement: Measurement, isNew: Boolean) extends Event

final case class ChemicalsListed(chemicals: List[Chemical]) extends Event
final case class ChemicalSaved(chemical: Chemical, isNew: Boolean) extends Event