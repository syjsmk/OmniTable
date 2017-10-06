package controllers

import javax.inject._

import domain.dao.RoomDAO
import domain.dao.impl.RoomDAOImpl
import domain.model.Room
import play.api._
import play.api.mvc._
import org.webjars.play.WebJarsUtil
import play.api.libs.json._

import scala.collection.mutable.ListBuffer

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class RobbyController @Inject()(cc: ControllerComponents, webJarsUtil: WebJarsUtil) extends AbstractController(cc) {

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


  def getRooms = Action {
    println("getRooms")

    val roomDAO: RoomDAO = new RoomDAOImpl()

    val rooms = roomDAO.getAll()
    rooms match {
//      case Some(s) =>
//        s.foreach(room => {
//          println(room.id)
//        })
//      case None => println("no rooms")
      case Some(s) => {

        var rooms = new ListBuffer[JsObject]()

//        println(rooms.size)
        s.foreach(room => {
          println("room.id : " + room.id + " room.name : " + room.name)
          val roomInfo = Json.obj("roomId" -> room.id, "roomName" -> room.name)
          println("ri : " + roomInfo)
          rooms += roomInfo
        })

//        println(rooms.size)
        rooms.foreach(room => {
          println(room)
        })

        Ok(Json.toJson(rooms.toList))

      }
      case None => Ok("no rooms")
    }

//    val roomInfo = Json.obj("roomId" -> "1")
//    val rooms = Json.arr(roomInfo)
//    Ok(rooms)
//    Ok("temp")
  }

//  def showRoom(id: Long) = Action {
//
//  }
}
