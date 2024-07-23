ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := Versions.scalaVersion

lazy val root = (project in file("."))
  .settings(
    name := "file-streaming-app"
  )

libraryDependencies ++= Libraries.allDeps