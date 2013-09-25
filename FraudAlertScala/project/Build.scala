import sbt._
import Keys._

object FraudAlertBuild extends Build {
  override lazy val settings = super.settings ++ Seq(
    organization := "com.pragmasoft",
    version := "1.0",
    scalaVersion in ThisBuild := "2.10.0",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "1.9.1" % "test",
      "org.mockito" % "mockito-core" % "1.9.0" % "test",
      "com.github.nscala-time" %% "nscala-time" % "0.6.0"
    )
  )

  lazy val myproject = Project(id= "FraudAlert", base = file("."))
}