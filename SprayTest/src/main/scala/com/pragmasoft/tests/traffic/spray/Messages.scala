package com.pragmasoft.tests.traffic.spray

case class ShutdownCommand(message: String)

case class MoveCommand(movesBatch: List[Move])

case class DispatchPositionCommand(position: Move)

case class DisplayTrafficInfo(trafficInfo: TrafficInfo)

case class AskPosition()

case class TellPosition(position: TaxiPosition)