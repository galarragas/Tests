name := "MeetingScheduler"

version := "1.0"

scalaVersion := "2.10.0"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0" % "test",
    "com.github.nscala-time" %% "nscala-time" % "0.2.0"
)

net.virtualvoid.sbt.graph.Plugin.graphSettings
