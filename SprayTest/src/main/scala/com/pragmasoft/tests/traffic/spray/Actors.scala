package com.pragmasoft.tests.traffic.spray

import akka.actor.{Props, ActorRef, Actor}
import scala.collection.mutable.ListBuffer
import DataTypes._
import org.joda.time.{Interval, DateTime}

object ActorPaths {
  val TAXI_ACTOR_SYSTEM_NAME = "taxi-actor-system"
  val ECHO_ACTOR_NAME = "echo"
  val TAXI_SUPERVISOR_ACTOR_NAME = "taxis"
  val ECHO_ACTOR_PATH = "/user/" + ECHO_ACTOR_NAME
  val TAXI_ACTOR_PATH_PREFIX = "akka://" + TAXI_ACTOR_SYSTEM_NAME + "/user/"

  def pathForTaxi(taxiID: TaxiID) = TAXI_ACTOR_PATH_PREFIX + taxiID
}

import ActorPaths._


class EchoActor extends Actor {
  def receive: EchoActor#Receive = {
    case DisplayTrafficInfo(trafficInfo) => Console.println(trafficInfo)
  }
}

class TaxiActor(id: TaxiID) extends Actor {
  Console.out.println("Creating TaxiActor: ", id, context.self)

  var lastMove: Move = Move("", Point(0.0, 0.0), new DateTime().withMillis(0l))

  def calculateSpeed(from: Move, to: Move): Double =
    (1000 * from.destination.distanceTo(to.destination)).toDouble / new Interval(from.time, to.time).toDurationMillis.toDouble

  def closeToTubeStation(point: Point): Boolean = true

  def doMove(move: Move) = {
    Console.println("Taxi: " + id + ", moving to " + move.destination + " at time " + move.time )

    if(closeToTubeStation(move.destination)) {
      val speedMeterPerSecond = calculateSpeed(lastMove, move)
      val displayActor = context.actorFor(ECHO_ACTOR_PATH)
      displayActor ! DisplayTrafficInfo(TrafficInfo(id, move.time, speedMeterPerSecond, TrafficCondition.HEAVY_TRAFFIC))
    }

    lastMove = move
  }

  def receive: TaxiActor#Receive = {
    case MoveCommand(moveBatch) => moveBatch.foreach(doMove)
    case ShutdownCommand(message) => { Console.out.printf("Shutting down agent %s with message '%s'", id, message)  }
    case AskPosition() => sender ! TellPosition(TaxiPosition(id, lastMove.time, lastMove.destination))
  }
}

object TaxiActor {
  val TAXI_1 : TaxiID = "taxi1"
  val TAXI_2 : TaxiID = "taxi2"

  val TAXI_IDS = List(TAXI_1, TAXI_2)

  def propsWithId(id: TaxiID): Props = Props(classOf[TaxiActor], id)
//    Props[TaxiActor].withCreator(new TaxiActor(id))
}
