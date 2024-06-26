val zioVersion = "2.1.5"
val zioHttpVersion = "0.0.5"
val zioLoggingVersion = "2.3.0"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.9-SNAPSHOT",
  scalaVersion := "3.5.0-RC2",
  libraryDependencies ++= {
    Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-json" % "0.7.1",
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
        "org.scalafx" %% "scalafx" % "22.0.0-R33"
         exclude("org.openjfx", "javafx-controls")
         exclude("org.openjfx", "javafx-fxml")
         exclude("org.openjfx", "javafx-graphics")
         exclude("org.openjfx", "javafx-media")
         exclude("org.openjfx", "javafx-swing")
         exclude("org.openjfx", "javafx-web"),
        "org.jfree" % "jfreechart" % "1.5.4",
        "com.formdev" % "flatlaf" % "3.4.1",
        "dev.zio" %% "zio-http" % zioHttpVersion
        exclude("dev.zio", "zio-streams"),
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "ch.qos.logback" % "logback-classic" % "1.5.6"
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
        "io.getquill" %% "quill-jdbc-zio" % "4.8.5",
        "dev.zio" %% "zio-cache" % "0.2.3",
        "org.postgresql" % "postgresql" % "42.7.3",
        "org.jodd" % "jodd-mail" % "7.0.1",
        compilerPlugin("com.github.ghik" % "zerowaste" % "0.2.21" cross CrossVersion.full),
        "dev.zio" %% "zio-test" % zioVersion % Test,
        "dev.zio" %% "zio-test-sbt" % zioVersion % Test
      )
    }
  )
