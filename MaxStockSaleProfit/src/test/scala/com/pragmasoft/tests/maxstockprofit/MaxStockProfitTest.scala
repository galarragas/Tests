package com.pragmasoft.tests.maxstockprofit

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import MaxStockProfit._

class MaxStockProfitTest extends FlatSpec with ShouldMatchers {
  "Profit" should "calculate the difference in value from the extremes in the interval" in {
    profit(Interval(Point(1.0, 20.2), Point(1.0, 30.2))) should equal(10.0)
  }

  "Min" should "return the point with min value" in {
    min(Point(1.0, 20.2), Point(1.0, 30.2)) should equal(Point(1.0, 20.2))
  }

  "CurrMax" should "return a status calculated using the only available point if no status available" in {
    val thePoint: Point = Point(1.0, 20.2)
    currMax(None, thePoint) should equal(Some(Status(Interval(thePoint, thePoint), thePoint)))
  }

  it should "return old max and changing the min if profit curve is decreasing below min" in {
    val optimum = Interval(Point(5.0, 100.1), Point(11.0, 1000.1))
    currMax(Some(Status(optimum, Point(5.0, 100.1))), Point(13.0, 90.1)) should
        equal(Some(Status(optimum, Point(13.0, 90.1))))
  }

  "MaxProfitPoint" should "return none for empty list" in {
    maxProfitPoint(List()) should equal(None)
  }

  it should "return single point for single element list" in {
    val thePoint: Point = Point(1.0, 20.2)
    maxProfitPoint(List(thePoint)) should equal(Some(Interval(thePoint, thePoint)))
  }

  it should "return first and last point in a monotone increasing curve" in {
    maxProfitPoint(List(Point(1.0, 20.2), Point(2.0, 21.2), Point(3.0, 22.2), Point(4.0, 23.2))) should
      equal(Some(Interval(Point(1.0, 20.2), Point(4.0, 23.2))))
  }

  it should "return best option in a more complex curve" in {
    val graph: List[Point] = List(
      Point(1.0, 120.2),
      Point(2.0, 111.2),
      Point(3.0, 82.2),
      Point(3.0, 102.2),
      Point(3.0, 92.2),
      Point(3.0, 112.2),
      Point(3.0, 72.2)
    )
    maxProfitPoint(graph) should equal(Some(Interval(Point(3.0, 82.2), Point(3.0, 112.2))))
  }
}
