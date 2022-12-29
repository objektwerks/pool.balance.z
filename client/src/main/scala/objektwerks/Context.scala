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
  val chart = conf.getString("chart")

  val pool = conf.getString("pool")
  val cleaning = conf.getString("cleaning")
  val measurement = conf.getString("measurement")
  val chemical = conf.getString("chemical")

  val pools = conf.getString("pools")
  val cleanings = conf.getString("cleanings")
  val measurements = conf.getString("measurements")
  val chemicals = conf.getString("chemicals")

  val headers = Headers (
    Header("Content-Type", "application/json; charset=utf-8"),
    Header("Accept", "application/json")
  )

  private def loadImage(path: String): Image =
    new ImageIcon(
      ImageIO.read( Context.getClass.getResourceAsStream(path) )
    ).getImage

  def logo: Image = loadImage("/logo.png")

  def init: Unit =
    if SystemInfo.isMacOS then
      System.setProperty("apple.laf.useScreenMenuBar", "true")
      System.setProperty("apple.awt.application.name", title)
      System.setProperty("apple.awt.application.appearance", "system")