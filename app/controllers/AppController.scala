package controllers

import javax.inject.{Inject, Singleton}

import org.webjars.play.WebJarsUtil
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class AppController @Inject()(cc: ControllerComponents, webJarsUtil: WebJarsUtil) extends AbstractController(cc) {
  def index = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(webJarsUtil))
  }
}
