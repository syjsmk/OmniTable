package controllers

import javax.inject._

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import domain.dao.RoomDAO
import domain.dao.impl.RoomDAOImpl
import domain.model.Room
import domain.model.Formatters._
import play.api._
import play.api.mvc._
import org.webjars.play.WebJarsUtil
import play.api.libs.json._
import play.api.libs.streams.ActorFlow
import services.RoomService

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class RobbyController @Inject()(roomDAO: RoomDAOImpl, roomService: RoomService, cc: ControllerComponents, webJarsUtil: WebJarsUtil)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.robby(webJarsUtil))
  }

  def getRooms() = Action.async {
    val rooms = roomService.getRooms()

    rooms.map(roomSeq => {
      Ok(Json.toJson(roomSeq))
    })
  }

  def makeRoom() = Action.async { implicit request: Request[AnyContent] =>

    val roomName = request.body.asFormUrlEncoded.get("roomName")

    val roomId = roomService.makeRoom(roomName(0))

    roomId.map(roomId => {
      Ok(Json.obj("id" -> roomId))
    })

  }

  def updateRoom(id: Int) = Action.async({ implicit request: Request[AnyContent] =>

    println("updateRoom")
    println(s"id: $id")
    val roomName = request.body.asFormUrlEncoded.get("roomName")
    println(roomName)

    val updatedRoom = roomService.updateRoom(id, roomName(0))

    updatedRoom.map(option => {
      option match {
        case Some(room) => {
          Ok(Json.obj("id" -> room.name))
        }
        case None => {
          Ok("not found")
        }
      }

    })

  })

  def deleteRoom(id: Int) = Action.async {

    println("deleteRoom")

    val deletedRoomId = roomService.deleteRoom(id)

    deletedRoomId.map(id => {
      Ok(Json.obj("id" -> id))
    })

  }

  def roomsWebSocket(): WebSocket = WebSocket.accept[String, String] { request =>

    ActorFlow.actorRef { out =>

      RoomsActor.props(out)
    }
  }

}


// class와 object의 이름을 똑같이 만드는건 관습적인 것?
object RoomsActor {
  var users = List[ActorRef]()
  def props(out: ActorRef) = Props(new RoomsActor(out))
}

class RoomsActor(actorRef: ActorRef) extends Actor {

  val OPEN = "!OPEN"
  val MAKE = "!MAKE"
  val UPDATE = "!UPDATE"
  val DELETE = "!DELETE"
  val CLOSE = "!CLOSE"

  def receive = {

    case CLOSE => {
      println(CLOSE)

      RoomsActor.users = RoomsActor.users.filterNot(user => user == actorRef)
      println("usersize : " + RoomsActor.users.size)

    }

    case OPEN => {

      println(OPEN)
      RoomsActor.users = (actorRef) :: RoomsActor.users
      println("usersize : " + RoomsActor.users.size)
      RoomsActor.users.foreach(_ ! OPEN)
    }

    case MAKE => {
      println(MAKE)
      RoomsActor.users.foreach(_ ! MAKE)
    }

    case UPDATE => {
      println(UPDATE)
      RoomsActor.users.foreach(_ ! UPDATE)
    }

    case DELETE => {
      println(DELETE)
      RoomsActor.users.foreach(_ ! DELETE)
    }

      // OPEN, CLOSE이외의 다른 메시지에 대해서는 이렇게 못하나?
//    case _ => {
//      println(_)
//      RoomsActor.users.foreach(user => {
//        user ! _
//      })
//    }


//    case msg =>
//
//      println("msg: " + msg)
//      RoomsActor.users = (actorRef) :: RoomsActor.users
//      actorRef ! ("I received your message: " + msg)
//
//      println("usersize : " + RoomsActor.users.size)
//
//      RoomsActor.users.foreach(_ ! msg)

  }
}