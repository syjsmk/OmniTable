package controllers

import javax.inject._

import domain.dao.RoomDAO
import domain.dao.impl.RoomDAOImpl
import domain.model.Room
import domain.model.Formatters._
import play.api._
import play.api.mvc._
import org.webjars.play.WebJarsUtil
import play.api.libs.json._
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
class RobbyController @Inject()(roomDAO: RoomDAOImpl, roomService: RoomService, cc: ControllerComponents, webJarsUtil: WebJarsUtil) extends AbstractController(cc) {

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

    //    val updatedRoom = roomService.updateRoom(Room(id, roomName(0)))
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


//    updatedRoom.map(option => {
////      Ok(Json.obj("id" -> option.getOrElse(0)))
//      Ok(Json.obj("id" => option.getOrElse(0)))
//    })
  })

  // val roomName = request.body.asFormUrlEncoded.get("roomName")

  //  def showRoom(id: Long) = Action {
  //
  //  }

  def deleteRoom(id: Int) = Action.async {

    println("deleteRoom")

    //TODO: 구현
    Future(Ok("delete"))
  }
}
