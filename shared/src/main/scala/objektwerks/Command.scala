package objektwerks

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

sealed trait Command

object Command:
  given JsonDecoder[Command] = DeriveJsonDecoder.gen[Command]
  given JsonEncoder[Command] = DeriveJsonEncoder.gen[Command]

  given JsonDecoder[ListPools] = DeriveJsonDecoder.gen[ListPools]
  given JsonEncoder[ListPools] = DeriveJsonEncoder.gen[ListPools]

final case class ListPools() extends Command
final case class AddPool(pool: Pool) extends Command
final case class UpdatePool(pool: Pool) extends Command

final case class ListCleanings() extends Command
final case class AddCleaning(cleaning: Cleaning) extends Command
final case class UpdateCleaning(cleaning: Cleaning) extends Command

final case class ListMeasurements() extends Command
final case class AddMeasurement(measurement: Measurement) extends Command
final case class UpdateMeasurement(measurement: Measurement) extends Command

final case class ListChemicals() extends Command
final case class AddChemical(chemical: Chemical) extends Command
final case class UpdateChemical(chemical: Chemical) extends Command