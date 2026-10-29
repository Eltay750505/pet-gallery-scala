val tapirVersion = "1.13.31"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "pet-gallery-scala",
    version := "0.1.0-SNAPSHOT",
    organization := "com.petgallery",
    scalaVersion := "3.9.0",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
      "org.http4s" %% "http4s-ember-server" % "0.23.38",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "ch.qos.logback" % "logback-classic" % "1.6.5",
      "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub4-server" % tapirVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.20" % Test,
      "com.softwaremill.sttp.client4" %% "circe" % "4.0.26" % Test
    )
  )
)
