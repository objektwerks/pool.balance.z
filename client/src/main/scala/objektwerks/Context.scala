package objektwerks

import com.formdev.flatlaf.util.SystemInfo

import java.awt.Image
import javax.imageio.ImageIO
import javax.swing.ImageIcon

import zio.http.model.{Header, Headers}

object Context:
  val conf = Resources.loadConfig("client.conf")

  val title = conf.getString("title")
  val width = conf.getInt("width")
  val height = conf.getInt("height")

  val host = conf.getString("host")
  val port = conf.getInt("port")
  val url = s"http://$host:$port/command"

  val add = conf.getString("add")
  val edit = conf.getString("edit")
  val save = conf.getString("save")
  val cancel = conf.getString("cancel")
  val close = conf.getString("close")
  val chart = conf.getString("chart")
  val date = conf.getString("date")

  val account = conf.getString("account")
  val pool = conf.getString("pool")
  val cleaning = conf.getString("cleaning")
  val measurement = conf.getString("measurement")
  val chemical = conf.getString("chemical")
  val faults = conf.getString("faults")

  val pools = conf.getString("pools")
  val cleanings = conf.getString("cleanings")
  val measurements = conf.getString("measurements")
  val chemicals = conf.getString("chemicals")

  val id = conf.getString("id")
  val poolId = conf.getString("poolId")
  val name = conf.getString("name")
  val volume = conf.getString("volume")
  val unit = conf.getString("unit")
  val brush = conf.getString("brush")
  val net = conf.getString("net")
  val skimmerBasket = conf.getString("skimmerBasket")
  val pumpBasket = conf.getString("pumpBasket")
  val pumpFilter = conf.getString("pumpFilter")
  val vacuum = conf.getString("vacuum")
  val cleaned = conf.getString("cleaned")
  val totalChlorine = conf.getString("totalChlorine")
  val freeChlorine = conf.getString("freeChlorine")
  val combinedChlorine = conf.getString("combinedChlorine")
  val ph = conf.getString("ph")
  val calciumHardness = conf.getString("calciumHardness")
  val totalAlkalinity = conf.getString("totalAlkalinity")
  val cyanuricAcid = conf.getString("cyanuricAcid")
  val totalBromine = conf.getString("totalBromine")
  val salt = conf.getString("salt")
  val temperature = conf.getString("temperature")
  val measured = conf.getString("measured")
  val typeof = conf.getString("typeof")
  val amount = conf.getString("amount")
  val added = conf.getString("added")

  val license = conf.getString("license")
  val activated = conf.getString("activated")
  val deactivated = conf.getString("deactivated")

  val headers = Headers (
    Header("Content-Type", "application/json; charset=utf-8"),
    Header("Accept", "application/json")
  )

  private def loadImage(path: String): Image =
    new ImageIcon(
      ImageIO.read( Context.getClass.getResourceAsStream(path) )
    ).getImage

  extension (value: String)
    def asLabel: String = s"$value:"
    def asHtml: String = s"<html>$value"
    def asHtmlWrap: String = s"<html>${value.replace(" ", "<br>")}"

  def logo: Image = loadImage("/logo.png")

  def init: Unit =
    if SystemInfo.isMacOS then
      System.setProperty("apple.laf.useScreenMenuBar", "true")
      System.setProperty("apple.awt.application.name", title)
      System.setProperty("apple.awt.application.appearance", "system")