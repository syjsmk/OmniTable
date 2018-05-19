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

//  def index() = Action { implicit request: Request[AnyContent] =>
//    Ok(views.html.robby(webJarsUtil))
//  }

  def getRooms() = Action.async {
    val rooms = roomService.getRooms()

    rooms.map(roomSeq => {
      Ok(Json.toJson(roomSeq))
    })
  }

  def makeRoom() = Action.async { implicit request: Request[AnyContent] =>

    val roomName = request.body.asFormUrlEncoded.get("roomName")
    val roomId = roomService.makeRoom(roomName(0), 0)

    roomId.map(roomId => {
      Ok(Json.obj("id" -> roomId))
    })

  }

  def updateRoom(id: Int) = Action.async({ implicit request: Request[AnyContent] =>

    println("updateRoom")
    println(s"id: $id")
    val roomName = request.body.asFormUrlEncoded.get("roomName")
    println(roomName)

    val NAME = "name"
    val updatedRoom = roomService.updateRoom(id, NAME, roomName(0))

    updatedRoom.map(option => {
      option match {
        case Some(room) => {
          Ok(Json.obj("id" -> room.id))
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

      RobbyActor.props(out)
    }
  }

}


// class와 object의 이름을 똑같이 만드는건 관습적인 것?
object RobbyActor {
  var users = List[ActorRef]()
  def props(out: ActorRef) = Props(new RobbyActor(out))
}

class RobbyActor(actorRef: ActorRef) extends Actor {

  val OPEN = "!OPEN"
  val MAKE = "!MAKE"
  val UPDATE = "!UPDATE"
  val DELETE = "!DELETE"
  val CLOSE = "!CLOSE"

  // 유저 사이즈 감소가 제대로 안되는 경우가 있는거 같음.
  def receive = {

    case CLOSE => {
      println(CLOSE)

      RobbyActor.users = RobbyActor.users.filterNot(user => user == actorRef)
      println("usersize : " + RobbyActor.users.size)

    }

    case OPEN => {

      println(OPEN)

      RobbyActor.users = (actorRef) :: RobbyActor.users

      println("usersize : " + RobbyActor.users.size)
      println(RobbyActor.users)
      RobbyActor.users.foreach(_ ! OPEN)
    }

    // make 했을 때 유저 수가 늘어나는 경우 있음?
    case MAKE => {
      println(MAKE)
      RobbyActor.users.foreach(_ ! MAKE)
    }

    case UPDATE => {
      println(UPDATE)
      RobbyActor.users.foreach(_ ! UPDATE)
    }

    case DELETE => {
      println(DELETE)
      RobbyActor.users.foreach(_ ! DELETE)
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