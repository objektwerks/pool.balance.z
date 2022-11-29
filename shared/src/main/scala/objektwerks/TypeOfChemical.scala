package objektwerks

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