val zioVersion = "2.1.21"
val zioHttpVersion = "3.5.1"
val zioLoggingVersion = "2.5.1"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.18-SNAPSHOT",
  scalaVersion := "3.7.3",
  libraryDependencies ++= {
    val jsoniterVersion = "2.37.10"
    Seq(
      "dev.zio" %% "zio" % zioVersion,
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % jsoniterVersion,
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % jsoniterVersion % Provided,
      "com.typesafe" % "config" % "1.4.3"
    )
  },
  scalacOptions ++= Seq(
    "-Wunused:all"
  )
)

lazy val poolbalance = (project in file("."))
  .aggregate(client, shared, server)
  .settings(common)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val client = project
  .enablePlugins(JavaAppPackaging)
  .dependsOn(shared)
  .settings(common)
  .settings(
    libraryDependencies ++= {
      Seq(
        "org.scalafx" %% "scalafx" % "24.0.2-R36"
         exclude("org.openjfx", "javafx-controls")
         exclude("org.openjfx", "javafx-fxml")
         exclude("org.openjfx", "javafx-graphics")
         exclude("org.openjfx", "javafx-media")
         exclude("org.openjfx", "javafx-swing")
         exclude("org.openjfx", "javafx-web"),
        "org.jfree" % "jfreechart" % "1.5.5",
        "com.formdev" % "flatlaf" % "3.6.1",
        "dev.zio" %% "zio-http" % zioHttpVersion
        exclude("dev.zio", "zio-streams"),
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "ch.qos.logback" % "logback-classic" % "1.5.18"
      )
    }
  )

lazy val shared = project
  .settings(common)

lazy val server = project
  .enablePlugins(JavaServerAppPackaging)
  .dependsOn(shared)
  .settings(common)
  .settings(
    libraryDependencies ++= {
      Seq(
        "dev.zio" %% "zio-http" % zioHttpVersion,
        "dev.zio" %% "zio-logging" % zioLoggingVersion,
        "dev.zio" %% "zio-logging-slf4j2" % zioLoggingVersion,
        "dev.zio" %% "zio-logging-slf4j2-bridge" % zioLoggingVersion,
        "io.getquill" %% "quill-jdbc-zio" % "4.8.6",
        "dev.zio" %% "zio-cache" % "0.2.4",
        "org.postgresql" % "postgresql" % "42.7.7",
        "org.jodd" % "jodd-mail" % "7.1.0",
        "dev.zio" %% "zio-test" % zioVersion % Test,
        "dev.zio" %% "zio-test-sbt" % zioVersion % Test
      )
    }
  )
