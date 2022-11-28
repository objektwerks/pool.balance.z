package objektwerks

sealed trait Command

final case class ListPools() extends Command
final case class AddPool(pool: Pool) extends Command
final case class UpdatePool(pool: Pool) extends Command
