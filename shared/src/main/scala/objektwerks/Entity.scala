package objektwerks

import java.time.{LocalDate, LocalDateTime}

sealed trait Entity:
  val id: Long

final case class Pool(id: Long = 0,
                      name: String = "", 
                      volume: Int = 0,
                      unit: UnitOfMeasure = UnitOfMeasure.gl) extends Entity

final case class Cleaning(id: Long = 0,
                          poolId: Long,
                          brush: Boolean = true,
                          net: Boolean = true,
                          skimmerBasket: Boolean = true,
                          pumpBasket: Boolean = false,
                          pumpFilter: Boolean = false,
                          vacuum: Boolean = false,
                          cleaned: LocalDateTime = LocalDateTime.now) extends Entity

final case class Measurement(id: Long = 0,
                             poolId: Long,
                             totalChlorine: Int = 3,
                             freeChlorine: Int = 3,
                             combinedChlorine: Double = 0.0,
                             ph: Double = 7.4,
                             calciumHardness: Int = 375,
                             totalAlkalinity: Int = 100,
                             cyanuricAcid: Int = 50,
                             totalBromine: Int = 5,
                             salt: Int = 3200,
                             temperature: Int = 85,
                             measured: LocalDateTime = LocalDateTime.now) extends Entity

final case class Chemical(id: Long = 0,
                          poolId: Long,
                          typeof: TypeOfChemical = TypeOfChemical.LiquidChlorine,
                          amount: Double = 1.0, 
                          unit: UnitOfMeasure = UnitOfMeasure.gl,
                          added: LocalDateTime = LocalDateTime.now) extends Entity

object Measurement:
  val totalChlorineRange = Range(1, 5).inclusive
  val freeChlorineRange = Range(1, 5).inclusive
  val combinedChlorineRange = Set(0.0, 0.1, 0.2)
  val phRange = Set(7.2, 7.3, 7.4, 7.5, 7.6)
  val calciumHardnessRange = Range(250, 500).inclusive
  val totalAlkalinityRange = Range(80, 120).inclusive
  val cyanuricAcidRange = Range(30, 100).inclusive
  val totalBromineRange = Range(2, 10).inclusive
  val saltRange = Range(2700, 3400).inclusive
  val temperatureRange = Range(50, 100).inclusive