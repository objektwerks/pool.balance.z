val zioVersion = "2.0.16"
val zioHttpVersion = "0.0.5"
val zioLoggingVersion = "2.1.14"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.9-SNAPSHOT",
  scalaVersion := "3.3.1",
  libraryDependencies ++= {
    Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-json" % "0.6.2",
      "com.typesafe" % "config" % "1.4.2"
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
        "org.scalafx" %% "scalafx" % "20.0.0-R31"
         exclude("org.openjfx", "javafx-controls")
         exclude("org.openjfx", "javafx-fxml")
         exclude("org.openjfx", "javafx-graphics")
         exclude("org.openjfx", "javafx-media")
         exclude("org.openjfx", "javafx-swing")
         exclude("org.openjfx", "javafx-web"),
        "org.jfree" % "jfreechart" % "1.5.4",
        "com.formdev" % "flatlaf" % "3.2.1",
        "dev.zio" %% "zio-http" % zioHttpVersion
        exclude("dev.zio", "zio-streams"),
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
        "ch.qos.logback" % "logback-classic" % "1.4.11"
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
        "io.getquill" %% "quill-jdbc-zio" % "4.6.1",
        "dev.zio" %% "zio-cache" % "0.2.3",
        "org.postgresql" % "postgresql" % "42.6.0",
        "org.jodd" % "jodd-mail" % "7.0.0",
        compilerPlugin("com.github.ghik" % "zerowaste" % "0.2.12" cross CrossVersion.full),
        "dev.zio" %% "zio-test" % zioVersion % Test,
        "dev.zio" %% "zio-test-sbt" % zioVersion % Test
      )
    }
  )
