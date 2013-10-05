package com.pragmasoft.tests.traffic.spray.rest

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import com.pragmasoft.tests.traffic.spray.{TaxiActor, EchoActor}
import com.pragmasoft.tests.traffic.spray.ActorPaths._
import com.pragmasoft.tests.traffic.spray.TaxiActor._

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem(TAXI_ACTOR_SYSTEM_NAME)

  val echoActor = system.actorOf(Props[EchoActor], ECHO_ACTOR_NAME)
  val taxy1Actor = system.actorOf(TaxiActor.propsWithId(TAXI_1), TAXI_1)
  val taxy2Actor = system.actorOf(TaxiActor.propsWithId(TAXI_2), TAXI_2)

  // create and start our service actor
  val service = system.actorOf(Props[TaxiServiceActor], "taxi-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)
}