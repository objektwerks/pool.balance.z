package objektwerks

import com.typesafe.config.Config

import io.getquill.*

import java.io.IOException
import java.sql.SQLException

import zio.{ZIO, ZLayer}

class Store(config: Config):
  val ctx = PostgresJdbcContext(Literal, config)
  import ctx.*

  inline def addPool(pool: Pool) =
    run( 
      query[Pool]
        .insertValue( lift(pool) )
        .returningGenerated(_.id)
    )

  inline def updatePool(pool: Pool) =
    run(
      query[Pool]
        .filter(_.id == lift(pool.id))
        .updateValue( lift(pool) )
    )

  inline def listPools = run( query[Pool] )

object DefaultStore:
  val layer: ZLayer[Any, IOException, Store] =
    ZLayer {
      for
        config <- Resources.loadConfig(path = "store.conf", section = "db")
      yield Store(config)
    }