package controllers

import javax.inject.{Inject, Singleton}

import org.webjars.play.WebJarsUtil
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.RoomService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class RoomController @Inject()(roomService: RoomService, cc: ControllerComponents, webJarsUtil: WebJarsUtil) extends AbstractController(cc) {

  def index(id: Int) = Action { implicit request: Request[AnyContent] =>

    println("RoomController index")
    println(id)

    Ok(views.html.room(webJarsUtil: WebJarsUtil, id))
  }

  def getRoom(id: Int) = Action.async {

    println(s"getRoom id: $id")

    val room = roomService.getRoom(id)

//    room.map(option => option match {
//      case Some(room) => {
//        Ok(Json.obj("id" -> room.id))
//      }
//      case None => {
//        Ok("None")
//      }
//    })

    room.map(_ match {
      case Some(room) => {
        Ok(Json.obj("id" -> room.id, "name" -> room.name))
      }
      case None => {
        Ok("None")
      }
    })

  }


  def changeUserCount(id: Int) = Action.async { implicit request: Request[AnyContent] =>

    val value = request.body.asFormUrlEncoded.get("value").head.toInt
    val updatedRoom = roomService.changeUserCount(id, value)

    updatedRoom.map(option => {
      option match {
        case Some(room) => {
          Ok(Json.obj("id" -> room.id, "name" -> room.name, "userCount" -> room.userCount))
        }
        case None => {
          Ok("None")
        }
      }
    })
  }
}
