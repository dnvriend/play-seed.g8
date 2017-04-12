name := "$name$"

organization := "$organization$"

version := "1.0.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.17"

// fp
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.10"
libraryDependencies += "com.github.mpilquist" %% "simulacrum" % "0.10.0"
//libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"

// compile-time DI (only used at compile-time so in "provided" scope)
libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"

// test
libraryDependencies += "org.typelevel" %% "scalaz-scalatest" % "1.1.2" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.6.8" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-M2" % Test

fork in Test := true

parallelExecution := false

enablePlugins(PlayScala)
