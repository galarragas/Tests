import sbt._
import Keys._
import ScalaMockPlugin._

object MyBuild extends Build {

  override lazy val settings = super.settings ++ Seq(
    organization := "com.pragmasoft",
    version := "1.0",
//    scalaVersion in ThisBuild := "2.10.0",
    scalaVersion in ThisBuild := "2.9.3",

    resolvers += ScalaToolsSnapshots,
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test",
    libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.0",
    autoCompilerPlugins := true
  )

  lazy val myproject = Project("ScalaTest", file(".")) settings(generateMocksSettings: _*) configs(Mock)
}