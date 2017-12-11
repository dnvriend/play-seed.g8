
import play.sbt.{PlayImport, PlayScala}
import sbt.Keys._
import sbt._

object ProjectSettings extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = plugins.JvmPlugin && SbtDapSchemas && PlayScala

  override def projectSettings = Seq(
    scalaVersion := "2.12.4",
    organization := "$organization$"
  ) ++ librarySettings

  lazy val librarySettings = Seq(
    libraryDependencies += PlayImport.guice,
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.17",
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4",
    libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.245"
  )
}
