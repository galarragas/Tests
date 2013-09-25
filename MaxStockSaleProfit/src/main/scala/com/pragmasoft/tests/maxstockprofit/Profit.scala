package com.pragmasoft.tests.maxstockprofit

case class Point(val x: Double, val y: Double)

case class Interval(val left: Point, val right: Point)

case class Status(val optimum: Interval, val graphMin: Point)

object MaxStockProfit {

  def profit(interval: Interval): Double = interval.right.y - interval.left.y

  def min(a: Point, b: Point) = if (a.y <= b.y) a else b

  def moreProfitable(a: Interval, b: Status): Interval = if (profit(a) > profit(b.optimum)) a else b.optimum

  def currMax(status: Option[Status], currPoint: Point): Option[Status] = status match {
    case None => Some(Status(Interval(currPoint, currPoint), currPoint))
    case Some(finderStatus) =>
      Some(
        Status(
          moreProfitable(Interval(finderStatus.graphMin, currPoint), finderStatus),
          min(currPoint, finderStatus.graphMin)
        )
      )
  }


  def maxProfitPoint(graph: List[Point]): Option[Interval] =
    graph.foldLeft[Option[Status]] (None) (currMax(_, _)) flatMap(a => Some(a.optimum))

}