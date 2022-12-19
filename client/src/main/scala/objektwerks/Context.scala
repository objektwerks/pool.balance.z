package objektwerks

object Context:
  val conf = Resources.loadConfig("client.conf")

  val name = conf.getString("name")

  val width = conf.getInt("width")
  val height = conf.getInt("height")

  val host = conf.getString("host")
  val port = conf.getInt("port")
  val url = s"http://$host:$port/command"