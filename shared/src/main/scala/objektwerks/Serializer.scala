package objektwerks

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

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

given JsonDecoder[Command] = DeriveJsonDecoder.gen[Command]
given JsonEncoder[Command] = DeriveJsonEncoder.gen[Command]

given JsonDecoder[ListPools] = DeriveJsonDecoder.gen[ListPools]
given JsonEncoder[ListPools] = DeriveJsonEncoder.gen[ListPools]

given JsonDecoder[AddPool] = DeriveJsonDecoder.gen[AddPool]
given JsonEncoder[AddPool] = DeriveJsonEncoder.gen[AddPool]

given JsonDecoder[UpdatePool] = DeriveJsonDecoder.gen[UpdatePool]
given JsonEncoder[UpdatePool] = DeriveJsonEncoder.gen[UpdatePool]

given JsonDecoder[ListCleanings] = DeriveJsonDecoder.gen[ListCleanings]
given JsonEncoder[ListCleanings] = DeriveJsonEncoder.gen[ListCleanings]

given JsonDecoder[AddCleaning] = DeriveJsonDecoder.gen[AddCleaning]
given JsonEncoder[AddCleaning] = DeriveJsonEncoder.gen[AddCleaning]

given JsonDecoder[UpdateCleaning] = DeriveJsonDecoder.gen[UpdateCleaning]
given JsonEncoder[UpdateCleaning] = DeriveJsonEncoder.gen[UpdateCleaning]

given JsonDecoder[Event] = DeriveJsonDecoder.gen[Event]
given JsonEncoder[Event] = DeriveJsonEncoder.gen[Event]