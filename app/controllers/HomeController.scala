package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import org.webjars.play.WebJarsUtil

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, webJarsUtil: WebJarsUtil) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(webJarsUtil))
  }


//  @()
//
//  @main("Welcome to Play") {
//    <h1>Welcome to Play!</h1>
//  }

//  @Html(webJarsUtil.script("babel.js"))
//  <script type='text/javascript' src='@routes.Assets.versioned("javascripts/reactTest.js")'></script>

}
