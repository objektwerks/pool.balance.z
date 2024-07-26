package objektwerks

import java.time.{LocalDate, ZoneId}
import java.util.{Date, UUID}

enum UnitOfMeasure:
  case gl, l, lb, kg, tablet

object UnitOfMeasure:
  def toList: List[String] = UnitOfMeasure.values.map(uom => uom.toString).toList
  def toPoolList: List[String] = List( UnitOfMeasure.gl.toString, UnitOfMeasure.l.toString )
  def selectedIndex(list: List[String], target: String): Option[(String, Int)] = list.zipWithIndex.find( (value, _) => value == target )
  def gallonsToLiters(gallons: Double): Double = gallons * 3.785
  def litersToGallons(liters: Double): Double = liters * 0.264
  def poundsToKilograms(pounds: Double): Double = pounds * 0.454
  def kilogramsToPounds(kilograms: Double): Double = kilograms * 2.205

enum TypeOfChemical(val display: String):
  case LiquidChlorine extends TypeOfChemical("Liquid Chlorine")
  case Trichlor extends TypeOfChemical("Trichlor")
  case Dichlor extends TypeOfChemical("Dichlor")
  case CalciumHypochlorite extends TypeOfChemical("Calcium Hypochlorite")
  case Stabilizer extends TypeOfChemical("Stabilizer")
  case Algaecide extends TypeOfChemical("Algaecide")
  case MuriaticAcid extends TypeOfChemical("Muriatic Acid")
  case Salt extends TypeOfChemical("Salt")

object TypeOfChemical:
  def toEnum(display: String): TypeOfChemical = TypeOfChemical.valueOf(display.filterNot(_.isWhitespace))
  def toList: List[String] = TypeOfChemical.values.map(toc => toc.display).toList
  def selectedIndex(target: String): Option[(String, Int)] = toList.zipWithIndex.find( (value, _) => value == target )

sealed trait Entity:
  val id: Long
  def toArray: Array[Any]

object Entity:
  def date(localDateAsLong: Long): Date =
    Date.from( LocalDate.ofEpochDay( localDateAsLong ).atStartOfDay( ZoneId.systemDefault ).toInstant() )

  def applyLocalDateChanges(sourceLocalDate: LocalDate, targetLocalDateAsLong: Long): Long =
    LocalDate.ofEpochDay(targetLocalDateAsLong)
      .withYear(sourceLocalDate.getYear)
      .withMonth(sourceLocalDate.getMonthValue)
      .withDayOfMonth(sourceLocalDate.getDayOfMonth)
      .toEpochDay

  given poolOrdering: Ordering[Pool] = Ordering.by[Pool, String](p => p.name).reverse
  given cleaningOrdering: Ordering[Cleaning] = Ordering.by[Cleaning, Long](c => c.cleaned).reverse
  given measurementOrdering: Ordering[Measurement] = Ordering.by[Measurement, Long](m => m.measured).reverse
  given chemicalOrdering: Ordering[Chemical] = Ordering.by[Chemical, Long](c => c.added).reverse

final case class Account(id: Long = 0,
                         emailAddress: String = "",
                         license: String = newLicense,
                         pin: String = Pin.newInstance,
                         activated: Long = LocalDate.now.toEpochDay,
                         deactivated: Long = 0) extends Entity derives CanEqual:
  def toArray: Array[Any] = Array(id, license, pin, activated, deactivated)

object Account:
  private def newLicense: String = UUID.randomUUID.toString

  val empty = Account(
    emailAddress = "",
    license = "",
    pin = "",
    activated = 0,
    deactivated = 0
  )

final case class Pool(id: Long = 0,
                      license: String = "",
                      name: String = "", 
                      volume: Int = 0,
                      unit: String = UnitOfMeasure.gl.toString) extends Entity derives CanEqual:
  def toArray: Array[Any] = Array(id, name, volume, unit)

final case class Cleaning(id: Long = 0,
                          poolId: Long,
                          brush: Boolean = true,
                          net: Boolean = true,
                          skimmerBasket: Boolean = true,
                          pumpBasket: Boolean = false,
                          pumpFilter: Boolean = false,
                          vacuum: Boolean = false,
                          cleaned: Long = LocalDate.now.toEpochDay) extends Entity derives CanEqual:
  def toArray: Array[Any] = Array(id, poolId, brush, net, skimmerBasket, pumpBasket, pumpFilter, vacuum, cleaned)

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
                             measured: Long = LocalDate.now.toEpochDay) extends Entity derives CanEqual:
  def toArray: Array[Any] = Array(id, poolId, totalChlorine, freeChlorine, combinedChlorine, ph, calciumHardness, totalAlkalinity, cyanuricAcid, totalBromine, salt, temperature, measured)

object Measurement:
  val totalChlorineRange = Range(1, 5).inclusive
  val freeChlorineRange = Range(1, 5).inclusive
  val combinedChlorineRange = Set(0.0, 0.1, 0.2, 0.3, 0.4, 0.5)
  val phRange = Set(6.2, 6.3, 6.4, 6.5, 6.6, 6.7, 6.8, 6.9, 7.0, 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7, 7.8, 7.9, 8.0, 8.1, 8.2, 8.3, 8.4)
  val calciumHardnessRange = Range(250, 500).inclusive
  val totalAlkalinityRange = Range(80, 120).inclusive
  val cyanuricAcidRange = Range(30, 100).inclusive
  val totalBromineRange = Range(2, 10).inclusive
  val saltRange = Range(2700, 3400).inclusive
  val temperatureRange = Range(50, 100).inclusive

final case class Chemical(id: Long = 0,
                          poolId: Long,
                          typeof: String = TypeOfChemical.LiquidChlorine.toString,
                          amount: Double = 1.0,
                          unit: String = UnitOfMeasure.gl.toString,
                          added: Long = LocalDate.now.toEpochDay) extends Entity derives CanEqual:
  def toArray: Array[Any] = Array(id, poolId, typeof, amount, unit, added)