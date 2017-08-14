package controllers

import javax.inject._

import play.api._
import play.api.mvc._
import org.webjars.play.WebJarsUtil
import play.api.libs.json.Json

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
    val roomInfo = Json.obj("roomId" -> "1")
    val rooms = Json.arr(roomInfo)
    Ok(rooms)
  }

//  def showRoom(id: Long) = Action {
//
//  }
}
