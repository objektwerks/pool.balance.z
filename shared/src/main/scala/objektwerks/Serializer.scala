package objektwerks

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

object Serializer:
  // Entity
  given JsonDecoder[Entity] = DeriveJsonDecoder.gen[Entity]
  given JsonEncoder[Entity] = DeriveJsonEncoder.gen[Entity]

  given JsonDecoder[Pool] = DeriveJsonDecoder.gen[Pool]
  given JsonEncoder[Pool] = DeriveJsonEncoder.gen[Pool]

  given JsonDecoder[Cleaning] = DeriveJsonDecoder.gen[Cleaning]
  given JsonEncoder[Cleaning] = DeriveJsonEncoder.gen[Cleaning]

  given JsonDecoder[Measurement] = DeriveJsonDecoder.gen[Measurement]
  given JsonEncoder[Measurement] = DeriveJsonEncoder.gen[Measurement]

  given JsonDecoder[Chemical] = DeriveJsonDecoder.gen[Chemical]
  given JsonEncoder[Chemical] = DeriveJsonEncoder.gen[Chemical]

  given JsonDecoder[TypeOfChemical] = DeriveJsonDecoder.gen[TypeOfChemical]
  given JsonEncoder[TypeOfChemical] = DeriveJsonEncoder.gen[TypeOfChemical]

  given JsonDecoder[UnitOfMeasure] = DeriveJsonDecoder.gen[UnitOfMeasure]
  given JsonEncoder[UnitOfMeasure] = DeriveJsonEncoder.gen[UnitOfMeasure]

  given JsonDecoder[Fault] = DeriveJsonDecoder.gen[Fault]
  given JsonEncoder[Fault] = DeriveJsonEncoder.gen[Fault]

  // Command
  given JsonDecoder[Command] = DeriveJsonDecoder.gen[Command]
  given JsonEncoder[Command] = DeriveJsonEncoder.gen[Command]

  given JsonDecoder[ListPools] = DeriveJsonDecoder.gen[ListPools]
  given JsonEncoder[ListPools] = DeriveJsonEncoder.gen[ListPools]

  given JsonDecoder[SavePool] = DeriveJsonDecoder.gen[SavePool]
  given JsonEncoder[SavePool] = DeriveJsonEncoder.gen[SavePool]

  given JsonDecoder[ListCleanings] = DeriveJsonDecoder.gen[ListCleanings]
  given JsonEncoder[ListCleanings] = DeriveJsonEncoder.gen[ListCleanings]

  given JsonDecoder[SaveCleaning] = DeriveJsonDecoder.gen[SaveCleaning]
  given JsonEncoder[SaveCleaning] = DeriveJsonEncoder.gen[SaveCleaning]

  given JsonDecoder[ListMeasurements] = DeriveJsonDecoder.gen[ListMeasurements]
  given JsonEncoder[ListMeasurements] = DeriveJsonEncoder.gen[ListMeasurements]

  given JsonDecoder[SaveMeasurement] = DeriveJsonDecoder.gen[SaveMeasurement]
  given JsonEncoder[SaveMeasurement] = DeriveJsonEncoder.gen[SaveMeasurement]

  given JsonDecoder[ListChemicals] = DeriveJsonDecoder.gen[ListChemicals]
  given JsonEncoder[ListChemicals] = DeriveJsonEncoder.gen[ListChemicals]

  given JsonDecoder[SaveChemical] = DeriveJsonDecoder.gen[SaveChemical]
  given JsonEncoder[SaveChemical] = DeriveJsonEncoder.gen[SaveChemical]

  // Event
  given JsonDecoder[Event] = DeriveJsonDecoder.gen[Event]
  given JsonEncoder[Event] = DeriveJsonEncoder.gen[Event]

  given JsonDecoder[PoolsListed] = DeriveJsonDecoder.gen[PoolsListed]
  given JsonEncoder[PoolsListed] = DeriveJsonEncoder.gen[PoolsListed]

  given JsonDecoder[PoolSaved] = DeriveJsonDecoder.gen[PoolSaved]
  given JsonEncoder[PoolSaved] = DeriveJsonEncoder.gen[PoolSaved]

  given JsonDecoder[CleaningsListed] = DeriveJsonDecoder.gen[CleaningsListed]
  given JsonEncoder[CleaningsListed] = DeriveJsonEncoder.gen[CleaningsListed]

  given JsonDecoder[CleaningSaved] = DeriveJsonDecoder.gen[CleaningSaved]
  given JsonEncoder[CleaningSaved] = DeriveJsonEncoder.gen[CleaningSaved]

  given JsonDecoder[MeasurementsListed] = DeriveJsonDecoder.gen[MeasurementsListed]
  given JsonEncoder[MeasurementsListed] = DeriveJsonEncoder.gen[MeasurementsListed]

  given JsonDecoder[MeasurementSaved] = DeriveJsonDecoder.gen[MeasurementSaved]
  given JsonEncoder[MeasurementSaved] = DeriveJsonEncoder.gen[MeasurementSaved]

  given JsonDecoder[ChemicalsListed] = DeriveJsonDecoder.gen[ChemicalsListed]
  given JsonEncoder[ChemicalsListed] = DeriveJsonEncoder.gen[ChemicalsListed]

  given JsonDecoder[ChemicalSaved] = DeriveJsonDecoder.gen[ChemicalSaved]
  given JsonEncoder[ChemicalSaved] = DeriveJsonEncoder.gen[ChemicalSaved]