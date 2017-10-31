package controllers

import javax.inject._

import domain.dao.RoomDAO
import domain.dao.impl.RoomDAOImpl
import domain.model.Room
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
class RobbyController @Inject()(ec: ExecutionContext, roomDAO: RoomDAOImpl, roomService: RoomService, cc: ControllerComponents, webJarsUtil: WebJarsUtil) extends AbstractController(cc) {

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

    val rooms = roomDAO.getAll()

    rooms.map(roomSeq => {

      println(s"roomSeq: $roomSeq")

      var roomInfos = new ListBuffer[JsObject]()
      roomSeq.foreach(room => {

        val roomInfo = Json.obj("roomId" -> room.id, "roomName" -> room.name)
        roomInfos += roomInfo
      })

//      Ok("qwe")
//        Ok(Json.obj("roomId" -> roomInfos))
      Ok(Json.toJson(roomInfos.toList))
    })
  }



    def makeRoom() = Action { implicit request: Request[AnyContent] =>

      val roomName = request.body.asFormUrlEncoded.get("roomName")
      roomName(0)
      println(roomName)
      println(roomName(0))

      val roomId = roomService.makeRoom(roomName(0))
      Ok(Json.obj("roomId" -> roomId))
    }

  //  def showRoom(id: Long) = Action {
//
//  }
}
