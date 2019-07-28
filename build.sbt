name := """tagless-final-pattern-for-scala-code"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.play" %% "play-slick" % "4.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2",
  "org.postgresql" % "postgresql" % "42.2.6"
)

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)