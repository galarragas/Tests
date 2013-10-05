package com.pragmasoft.tests.traffic.spray


package object DataTypes {
  type TaxiID = String
}

case class Point(latitude: Double, longitude: Double) {
  val DEGREES_TO_METER_FACTOR = 60 * 1.1515 * 1609.344

  def distanceTo(that: Point): Int = {
    val theta: Double = this.longitude - that.longitude
    var dist: Double = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(that.latitude)) + Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(that.latitude)) * Math.cos(deg2rad(theta))
    dist = Math.acos(dist)
    dist = rad2deg(dist)
    dist = dist * DEGREES_TO_METER_FACTOR
    return Math.floor(dist).asInstanceOf[Int]
  }

  private def deg2rad(deg: Double): Double = {
    return (deg * Math.PI / 180.0)
  }

  private def rad2deg(rad: Double): Double = {
    return (rad * 180.0 / Math.PI)
  }
}

import DataTypes._
import org.joda.time.DateTime

case class Move(taxiId: TaxiID, destination: Point, time: DateTime)

object TrafficCondition extends Enumeration {
  type TrafficCondition = Value
  val HEAVY_TRAFFIC, LIGHT_TRAFFIC, MODERATE_TRAFFIC = Value
}

import TrafficCondition._

case class TrafficInfo(agentId: String, time: DateTime, speed: Double, trafficCondition: TrafficCondition)

case class TaxiPosition(taxiId: TaxiID, time: DateTime, position: Point)