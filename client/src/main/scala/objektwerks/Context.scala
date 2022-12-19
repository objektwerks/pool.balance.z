package objektwerks

object Context:
  val conf = Resources.loadConfig("client.conf")

  val name = conf.getString("name")
  
  val width = conf.getInt("width")
  val height = conf.getInt("height")