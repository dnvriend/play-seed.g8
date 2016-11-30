name := "$name$"

organization := "$organization$"

version := "1.0.0"

scalaVersion := "2.11.8"
scalaOrganization := "org.typelevel"
scalacOptions += "-Ypartial-unification" // enable fix for SI-2712
scalacOptions += "-Yliteral-types"       // enable SIP-23 implementation

libraryDependencies += ws
libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.7"
libraryDependencies += "org.typelevel" %% "scalaz-outlaws" % "0.2"
libraryDependencies += "com.chuusai" %% "shapeless" % "2.3.2"
libraryDependencies += "com.h2database" % "h2" % "1.4.193"
libraryDependencies += "com.zaxxer" % "HikariCP" % "2.5.1"
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.2"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.5.10"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.4.14"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.14.0"
libraryDependencies += "org.typelevel" %% "scalaz-scalatest" % "1.1.0" % Test  
libraryDependencies += "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.15" % Test  
libraryDependencies += "org.mockito" % "mockito-core" % "2.2.21" % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0-M1" % Test

fork in Test := true
parallelExecution := false

licenses +=("Apache-2.0", url("http://opensource.org/licenses/apache2.0.php"))

// enable scala code formatting //
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import sbtbuildinfo.BuildInfoKey.action

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentClassDeclaration, true)

// enable updating file headers //
import de.heikoseeberger.sbtheader.license.Apache2_0

headers := Map(
  "scala" -> Apache2_0("2016", "$author_name$"),
  "conf" -> Apache2_0("2016", "$author_name$", "#")
)

// buildinfo
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
//buildInfoKeys += action("lastCommitHash") { "git rev-parse HEAD".!!.trim }
buildInfoPackage := "$package$.component.buildinfo"
buildInfoKeys += buildInfoBuildNumber
buildInfoOptions += BuildInfoOption.ToMap
buildInfoOptions += BuildInfoOption.ToJson
buildInfoOptions += BuildInfoOption.BuildTime

// conductr
BundleKeys.endpoints := Map(
  "play" -> Endpoint(bindProtocol = "http", bindPort = 0, services = Set(URI("http://:9000/$name$"))),
  "akka-remote" -> Endpoint("tcp")
)

normalizedName in Bundle := "$name$"

BundleKeys.system := "$actor_system_name$"

BundleKeys.startCommand += "-Dhttp.address=\$PLAY_BIND_IP -Dhttp.port=\$PLAY_BIND_PORT -Dplay.akka.actor-system=\$BUNDLE_SYSTEM"

enablePlugins(AutomateHeaderPlugin, SbtScalariform, PlayScala, BuildInfoPlugin, ConductrPlugin)
