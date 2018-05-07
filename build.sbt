name := """OmniTable"""
organization := "com.s2org"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test,
  "org.webjars" % "react" % "15.2.1",
  "org.webjars" %% "webjars-play" % "2.6.1",
//  "org.webjars" % "babel" % "6.3.26-oo1",
  "org.webjars" % "jquery" % "3.2.1",

  // slick
  "com.typesafe.play" %% "play-slick" % "3.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0",

  // https://mvnrepository.com/artifact/mysql/mysql-connector-java
  "mysql" % "mysql-connector-java" % "5.1.44",

  // bootstrap
  "org.webjars" % "bootstrap" % "4.0.0-alpha"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
JsEngineKeys.engineType := JsEngineKeys.EngineType.Node