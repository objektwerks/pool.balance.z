ThisBuild / organization := "objektwerks"
ThisBuild / version := "0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1"

lazy val scalaTestVersion = "3.2.14"

lazy val root = (project in file("."))
  .aggregate(client, shared, server)

lazy val client = project
  .enablePlugins(JavaAppPackaging)
  .dependsOn(shared)
  .settings(
    mainClass in Compile := Some("objektwerks.Client")
  )

lazy val shared = project

lazy val server = project
  .enablePlugins(JavaServerAppPackaging)
  .dependsOn(shared)
  .settings(
    libraryDependencies ++= {
      Seq(
        "org.scalatest" %% "scalatest" % scalaTestVersion % Test
      )
    }
  )