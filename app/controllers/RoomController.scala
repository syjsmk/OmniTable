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
import services.RoomService
import scala.collection.mutable.Map

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoomController @Inject()(roomService: RoomService, cc: ControllerComponents, webJarsUtil: WebJarsUtil)(implicit actorSystem: ActorSystem, materializer: Materializer) extends AbstractController(cc) {

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
      RoomActor.props(out, id)
    }
  }
  }
}

object RoomActor {
  var membersMap = Map[Int, List[ActorRef]]()
  def props(out: ActorRef, id: Int) = Props(new RoomActor(out, id))
}

class RoomActor(actorRef: ActorRef, id: Int) extends Actor {

  val JOIN = "!JOIN"
  val EXIT = "!EXIT"

  override def receive = {

    case JOIN => {
      println("!JOIN")

      RoomActor.membersMap.get(id) match {
        case Some(members) => {
//          println(members)
//          println(members.size)

          val updatedMembers = actorRef :: members
          updatedMembers.foreach(actor => {
            actor ! JOIN
          })

          RoomActor.membersMap = RoomActor.membersMap.updated(id, updatedMembers)

        }

        case None => {
          println("Room member is not exist")

          RoomActor.membersMap += (id -> List(actorRef))
          actorRef ! JOIN
        }
      }


      println(RoomActor.membersMap)

    }


    case EXIT => {

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

  }

}