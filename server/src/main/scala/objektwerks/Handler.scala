package objektwerks

import zio.{ZIO, ZLayer}
import zio.json.{DecoderOps, EncoderOps}

import Serializer.given

final case class Handler():
  def handle(request: Either[String, Command]): ZIO[Any, Nothing, String] =
    ZIO.succeed(
      request match
        case Right(command) => command match // TODO! Authorizer, Validator, Handler, Store
          case ListPools()                  => PoolsListed(Nil).toJson
          case SavePool(pool)               => PoolSaved(0L).toJson
          case ListCleanings()              => CleaningsListed(Nil).toJson
          case SaveCleaning(cleaning)       => CleaningSaved(0L).toJson
          case ListMeasurements()           => MeasurementsListed(Nil).toJson
          case SaveMeasurement(measurement) => MeasurementSaved(0L).toJson
          case ListChemicals()              => ChemicalsListed(Nil).toJson
          case SaveChemical(chemical)       => ChemicalSaved(0L).toJson
        case Left(cause) => Fault(cause).toJson
    )

object Handler:
  val layer: ZLayer[Any, Nothing, Handler] = ZLayer.succeed(apply())