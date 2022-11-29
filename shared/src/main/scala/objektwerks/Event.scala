package objektwerks

import java.time.Instant
import java.time.format.DateTimeFormatter

sealed trait Event

final case class PoolsListed(pools: List[Pool]) extends Event
final case class PoolSaved(pool: Pool) extends Event

final case class CleaningsListed(cleanings: List[Cleaning]) extends Event
final case class CleaningSaved(cleaning: Cleaning) extends Event

final case class MeasurementsListed(measurements: List[Measurement]) extends Event
final case class MeasurementSaved(measurement: Measurement) extends Event

final case class ChemicalsListed(chemicals: List[Chemical]) extends Event
final case class ChemicalSaved(chemical: Chemical) extends Event

final case class Fault(cause: String,
                       occurred: String = Entity.instant) extends Event