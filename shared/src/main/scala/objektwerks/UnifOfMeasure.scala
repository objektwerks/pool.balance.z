package objektwerks

enum UnitOfMeasure:
  case gl, l, lb, kg, tablet

object UnitOfMeasure:
  def toList: List[String] = UnitOfMeasure.values.map(uom => uom.toString).toList
  def toPoolList: List[String] = List( UnitOfMeasure.gl.toString, UnitOfMeasure.l.toString )
  def gallonsToLiters(gallons: Double): Double = gallons * 3.785
  def litersToGallons(liters: Double): Double = liters * 0.264
  def poundsToKilograms(pounds: Double): Double = pounds * 0.454
  def kilogramsToPounds(kilograms: Double): Double = kilograms * 2.205