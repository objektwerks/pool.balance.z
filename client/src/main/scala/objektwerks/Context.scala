package objektwerks

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

  val headers = Headers (
    Header("Content-Type", "application/json; charset=utf-8"),
    Header("Accept", "application/json")
  )

  def loadImage(path: String): Image =
    loadImageIcon(path).getImage()

  def loadImageIcon(path: String): ImageIcon =
    new ImageIcon(
      ImageIO.read( Context.getClass.getResourceAsStream(path) )
    )

  def logoImage = loadImage("/image/logo.png")
  def addImageIcon = loadImageIcon("/image/add.png")
  def editImageIcon = loadImageIcon("/image/edit.png")
  def chartImageIcon = loadImageIcon("/image/chart.png")
  def errorsImageIcon = loadImageIcon("/image/errors.png")
