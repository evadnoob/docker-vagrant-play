name := "time-in-timezone"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "joda-time" % "joda-time" % "2.3",
  "org.joda" % "joda-convert" % "1.2"
)



