lazy val root = (project in file("."))
  .settings(
    organization := "objektwerks",
    version := "0.1-SNAPSHOT",
    scalaVersion := "3.2.1"
  )
  .aggregate(client, shared, server)

lazy val client = project.
  dependsOn(shared)

lazy val shared = project

lazy val server = project.
  dependsOn(shared)