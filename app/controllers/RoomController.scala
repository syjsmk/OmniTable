package controllers

import javax.inject.{Inject, Singleton}

import akka.actor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import domain.model.{DataWriter, Room}
import org.webjars.play.WebJarsUtil
import play.api.libs.json.{Json, Writes}
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.{MessageService, RoomService}

import scala.collection.mutable.Map
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoomController @Inject()(roomService: RoomService, messageService: MessageService, cc: ControllerComponents, webJarsUtil: WebJarsUtil)(implicit actorSystem: ActorSystem, materializer: Materializer) extends AbstractController(cc) {

  val dataWriter = new DataWriter()
  var roomActors = Seq()

  def index(id: Int) = Action { implicit request: Request[AnyContent] =>

    println("RoomController index")
    println(id)

    Ok(views.html.room(webJarsUtil: WebJarsUtil, id))
  }


  def getRoom(id: Int) = Action.async {

    println(s"getRoom id: $id")

    val room = roomService.getRoom(id)

    room.map(_ match {
      case Some(room) => {
        Ok(Json.toJson(room)(dataWriter.roomWrites))
      }
      case None => {
        Ok("None")
      }
    })

  }


  def roomWebSocket(id: Int): WebSocket = WebSocket.accept[String, String] { request => {

//    println(s"roomWebSocketId: $id")

    ActorFlow.actorRef { out =>
//      RoomActor.props(out, id)
      RoomActor.props(out, id, messageService)
    }
  }
  }
}

object RoomActor {
  var membersMap = Map[Int, List[ActorRef]]()
  def props(out: ActorRef, id: Int, messageService: MessageService) = Props(new RoomActor(out, id, messageService))
}

class RoomActor(actorRef: ActorRef, id: Int, messageService: MessageService) extends Actor {

  val JOIN = "!JOIN"
  val EXIT = "!EXIT"
  val MESSAGE = "!MESSAGE"

  override def receive = {

    case receivedData: String => {

      val data = Json.parse(receivedData)
      val messageType = (data \ "type").get.as[String]

      messageType match {

        case JOIN => {
          println("!JOIN")

          RoomActor.membersMap.get(id) match {
            case Some(members) => {
              //          println(members)
              //          println(members.size)

              val updatedMembers = actorRef :: members
              updatedMembers.foreach(actor => {
//                actor ! JOIN
                actor ! Json.obj("type" -> JOIN).toString()
              })

              RoomActor.membersMap = RoomActor.membersMap.updated(id, updatedMembers)

            }

            case None => {
              println("Room member is not exist")

              RoomActor.membersMap += (id -> List(actorRef))
//              actorRef ! JOIN
              actorRef ! Json.obj("type" -> JOIN).toString()
            }
          }


          println(RoomActor.membersMap)

        }

        case MESSAGE => {

          val time = (data \ "time").get.as[String]
          val sender = (data \ "sender").get.as[String]
          val message = (data \ "message").get.as[String]

//          println(s"time: $time, sender: $sender, message: $message")

          RoomActor.membersMap.get(id) match {
            case Some(members) => {
//              members.foreach(_ ! MESSAGE)

              // TODO: 여기서 toString으로 보내고 프론트엔드에서 JSON.parse를 해서 쓰는게 적절한 처리인가?
              val messageObject = Json.obj("type" -> MESSAGE, "time" -> time, "sender" -> sender, "message" -> message)
              members.foreach(_ ! messageObject.toString())

              // TODO: 여기서 해당 메시지 DB 저장 및 room에 join시 DB에서 메시지 읽어오게 해야 함
              messageService.saveMessage(messageObject)
            }
            case None => {
              println("Room member is not exist")
            }
          }
        }


        case EXIT => {

          println(EXIT)

          var changedMembers = List[ActorRef]()

          RoomActor.membersMap.get(id) match {
            case Some(members) => {
              changedMembers = members.filterNot(user => user == actorRef)

              if(changedMembers.isEmpty) {
                RoomActor.membersMap.remove(id)
              } else {
                RoomActor.membersMap.update(id, changedMembers)
              }
            }

            case None => {
              println("Room member is not exist")
            }
          }

        }

        case _ => {
          println("not defined case")
        }

      }

    }

  }

}