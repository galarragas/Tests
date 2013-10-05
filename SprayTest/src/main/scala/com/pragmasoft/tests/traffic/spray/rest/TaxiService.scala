package com.pragmasoft.tests.traffic.spray.rest

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.json._
import com.pragmasoft.tests.traffic.spray._
import org.joda.time.{DateTime => JodaTime}
import spray.httpx.SprayJsonSupport
import com.pragmasoft.tests.traffic.spray.DataTypes._
import com.pragmasoft.tests.traffic.spray.Point
import com.pragmasoft.tests.traffic.spray.MoveCommand
import com.pragmasoft.tests.traffic.spray.Move
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._


object MyJsonProtocol extends DefaultJsonProtocol {

  implicit object jodaDateFormat extends RootJsonFormat[JodaTime] {
    def write(obj: JodaTime): JsValue = JsObject("epoch" -> JsNumber(obj.getMillis))

    def read(json: JsValue): JodaTime = json.asJsObject.getFields("epoch") match {
      case Seq(JsNumber(epoch)) => new JodaTime(epoch.toLong)
      case _ => throw new DeserializationException("Joda DateTime expected")
    }
  }

  implicit val pointFormat = jsonFormat(Point, "latitude", "longitude")
  implicit val moveFormat = jsonFormat3(Move)
  implicit val taxiPositionFormat = jsonFormat3(TaxiPosition)
}


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class TaxiServiceActor extends Actor with TaxiService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait TaxiService extends HttpService with SprayJsonSupport {

  import ActorPaths._
  import akka.pattern.ask
  import MyJsonProtocol._
  import com.pragmasoft.tests.traffic.spray.ActorPaths._

  val myRoute =
    path("taxi" / "move") {
      post {
        entity(as[Move]) {
          move =>
            respondWithMediaType(`text/html`) {
              forwardMove(move)

              // XML is marshalled to `text/xml` by default, so we simply override here
              complete {
                <html>
                  <body>
                    <h1>Message sent to taxi #
                      <i>
                        {move.taxiId}
                      </i>
                      !</h1>
                    Move is
                    <br/>{move}
                  </body>
                </html>
              }
            }
        }
      }
    } ~
      path("shutdown") {
        post {
          complete {
            Console.println("Shutdnown")

            "shutting down"
          }
        }
      } ~
      path("taxi" / Segment / "position") {
        taxiId =>
          get {
            implicit val timeout = Timeout(10 seconds)

            Console.println("Getting position of taxy ", taxiId)

            val taxiPosition = Await.result((getTaxi(taxiId) ask AskPosition()).mapTo[TellPosition], (10 seconds))

            complete(taxiPosition.position)
          }
      }

  def getTaxi(taxiId: TaxiID) = actorRefFactory.actorSelection(pathForTaxi(taxiId))

  def forwardMove(move: Move) {
    val destination = getTaxi(move.taxiId)
    destination ! MoveCommand(List(move))
  }
}