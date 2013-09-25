import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "PlayTest1"
  val appVersion = "1.0-SNAPSHOT"


  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "se.radley" %% "play-plugins-salat" % "1.2",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test",
    "org.mockito" % "mockito-all" % "1.9.0" % "test",
    "com.github.athieriot" %% "specs2-embedmongo" % "0.5.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    routesImport += "se.radley.plugin.salat.Binders._",
    templatesImport += "org.bson.types.ObjectId",
    resolvers += Resolver.sonatypeRepo("snapshots")
  )

}
