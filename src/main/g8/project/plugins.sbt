// to enable the playframework
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.7")

// code formatting
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1")

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.17"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.245"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
