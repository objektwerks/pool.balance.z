package objektwerks

import com.typesafe.config.{Config, ConfigFactory}

import io.getquill.*
import io.getquill.jdbczio.Quill.H2

import java.sql.SQLException

import zio.{ZIO, ZLayer}

trait Store:
  def addPool(pool: Pool): ZIO[Any, SQLException, Int]
  def updatePool(pool: Pool): ZIO[Any, SQLException, Long]
  def listPools: ZIO[Any, SQLException, List[Pool]]

case class DefaultStore(config: Config) extends Store:
  val ctx = H2(SnakeCase, new H2JdbcContext(SnakeCase, config).dataSource)
  import ctx.*

  inline def addPool(pool: Pool): ZIO[Any, SQLException, Long] =
    run( 
      query[Pool]
        .insertValue( lift(pool) )
        .returningGenerated(_.id)
    )

  inline def updatePool(pool: Pool): ZIO[Any, SQLException, Long] =
    run(
      query[Pool]
        .filter(_.id == lift(pool.id))
        .updateValue( lift(pool) )
    )

  inline def listPools: ZIO[Any, SQLException, List[Pool]] = run( query[Pool] )

object DefaultStore:
  val layer: ZLayer[Any, IOException, Store] =
    ZLayer {
      for
        config <- Resources.loadConfig(path = "quill.conf", section = "quill.ctx")
      yield DefaultStore(config)
    }