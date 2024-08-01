package objektwerks

import java.time.LocalDate

sealed trait Event

final case class Registered(account: Account) extends Event derives CanEqual
final case class LoggedIn(account: Account) extends Event derives CanEqual

final case class Deactivated(account: Account) extends Event derives CanEqual
final case class Reactivated(account: Account) extends Event derives CanEqual

final case class PoolsListed(pools: List[Pool]) extends Event derives CanEqual
final case class PoolSaved(id: Long) extends Event derives CanEqual

final case class CleaningsListed(cleanings: List[Cleaning]) extends Event derives CanEqual
final case class CleaningSaved(id: Long) extends Event derives CanEqual

final case class MeasurementsListed(measurements: List[Measurement]) extends Event derives CanEqual
final case class MeasurementSaved(id: Long) extends Event derives CanEqual

final case class ChemicalsListed(chemicals: List[Chemical]) extends Event derives CanEqual
final case class ChemicalSaved(id: Long) extends Event derives CanEqual

final case class Fault(cause: String, occurred: Long = LocalDate.now.toEpochDay) extends Event derives CanEqual