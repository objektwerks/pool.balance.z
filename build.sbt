val zioVersion = "2.0.5"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.1-SNAPSHOT",
  scalaVersion := "3.2.1",
  libraryDependencies ++= {
    Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-json" % "0.3.0",
      "com.typesafe" % "config" % "1.4.2"
    )
  }
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
        "org.jfree" % "jfreechart" % "1.5.3",
        "com.miglayout" % "miglayout-swing" % "11.0",
        "dev.zio" %% "zio-http" % "0.0.3"
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
        "dev.zio" %% "zio-http" % "0.0.3",
        "dev.zio" %% "zio-logging" % "2.1.5",
        "io.getquill" %% "quill-jdbc-zio" % "4.6.0",
        "dev.zio" %% "zio-cache" % "0.2.1",
        "org.postgresql" % "postgresql" % "42.5.1",
        compilerPlugin("com.github.ghik" % "zerowaste" % "0.2.1" cross CrossVersion.full),
        "dev.zio" %% "zio-test" % zioVersion % Test,
        "dev.zio" %% "zio-test-sbt" % zioVersion % Test
      )
    }
  )