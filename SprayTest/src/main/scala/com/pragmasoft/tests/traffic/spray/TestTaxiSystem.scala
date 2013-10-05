package com.pragmasoft.tests.traffic.spray

import akka.actor.{Props, ActorSystem}
import ActorPaths._
import org.joda.time.DateTime

object TestTaxiSimpleApp extends App {
  val system = ActorSystem("TaxiSimpleApp")

  val echoActor = system.actorOf(Props[EchoActor], ECHO_ACTOR_NAME)

//  dispatcherActor ! DispatchPositionCommand(Move(TaxiActor.TAXI_1, Point(12.133, -99.444), new DateTime().withMillis(1000l)))
//  dispatcherActor ! DispatchPositionCommand(Move(TaxiActor.TAXI_1, Point(13.133, -99.444), new DateTime().withMillis(2000l)))
//  dispatcherActor ! DispatchPositionCommand(Move(TaxiActor.TAXI_1, Point(14.133, -99.444), new DateTime().withMillis(3000l)))
//  dispatcherActor ! DispatchPositionCommand(Move(TaxiActor.TAXI_1, Point(15.133, -99.444), new DateTime().withMillis(4000l)))

  //  val taxiPositionFuture = system.actorFor(pathForTaxi(TaxiActor.TAXI_1)). ? AskPosition()

  Console.println("Sending shut down message to taxi supervisor")
//  taxiSupervisor ! ShutdownCommand("Shutting down")

  Console.println("Shutting down")

  system.shutdown()
}
