ThisBuild / organization := "objektwerks"
ThisBuild / version := "0.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .aggregate(client, shared, server)

lazy val client = project
  .dependsOn(shared)

lazy val shared = project

lazy val server = project
  .dependsOn(shared)