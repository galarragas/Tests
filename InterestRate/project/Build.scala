import sbt._
import sbt.Keys._

object MyBuild extends Build {
  val opts = Project.defaultSettings ++ Seq(
    scalaVersion := "2.10.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )

  lazy val root =
    Project(id = "InterestRate", base=file("."), settings=(opts) )

//  lazy val myproject = Project("MaxStockSaleProfit", file(".")) settings (generateMocksSettings: _*) configs (Mock)
}