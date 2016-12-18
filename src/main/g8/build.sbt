name := "$name$"

organization := "$organization$"

version := "1.0.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.mockito" % "mockito-core" % "2.2.21" % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0-M1" % Test

fork in Test := true

parallelExecution := false

enablePlugins(PlayScala)
