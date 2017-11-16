package controllers

import javax.inject.{Inject, Singleton}

import org.webjars.play.WebJarsUtil
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.RoomService

import scala.concurrent.ExecutionContext.Implicits.global

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
}
