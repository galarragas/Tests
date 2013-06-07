package com.pragmasoft.tests.maxstockprofit

case class ProfitGraphPoint(val x: Double, val y: Double)
case class ProfitGraphInterval(val left: ProfitGraphPoint, val right: ProfitGraphPoint)
case class MaxFinderStatus(val optimum: ProfitGraphInterval, val graphMin: ProfitGraphPoint)

object ProfitGraphPoint {
}

object ProfitGraphInterval {
}

object MaxFinderStatus {
}

object MaxStockProfit {

  def profit(interval: ProfitGraphInterval): Double = interval.right.y - interval.left.y

  def min(a: ProfitGraphPoint, b: ProfitGraphPoint) = if(a.y <= b.y) a else b

  def currMax(status: Option[MaxFinderStatus], currPoint: ProfitGraphPoint): Option[MaxFinderStatus] = status match {
    case None => Some(MaxFinderStatus(ProfitGraphInterval(currPoint, currPoint), currPoint))
    case Some(finderStatus) => {
      val potentialMaxInterval: ProfitGraphInterval = ProfitGraphInterval(finderStatus.graphMin, currPoint)
      Some(
        MaxFinderStatus(
          if (profit(potentialMaxInterval) > profit(finderStatus.optimum)) potentialMaxInterval else finderStatus.optimum,
          min(currPoint, finderStatus.graphMin)
        )
      )
    }
  }

  def maxProfitPoint(graph: List[ProfitGraphPoint]): Option[ProfitGraphInterval] =
    graph.foldLeft[Option[MaxFinderStatus]](None)(currMax(_,_)).flatMap(a => Some(a.optimum))

}