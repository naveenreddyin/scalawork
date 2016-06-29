package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.User
import models.UserProfile


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Authentication @Inject() extends Controller {

  val registerForm = Form(
    tuple(
      "user" -> mapping(
        "email" -> nonEmptyText,
        "password" -> nonEmptyText
        )(User.apply)(User.unapply),
      "profile" -> mapping(
        "firstname"->nonEmptyText,
        "lastname"->nonEmptyText,
        "gender" -> ignored(0)
      )(UserProfile.apply)(UserProfile.unapply))
    )

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def login = Action {
    Ok(views.html.login())
  }

  def loginSubmit = Action {
  	implicit request => 
  	// val maybeFoo = request.body.asFormUrlEncoded.get("password").lift(0) // returns an Option[String]
  	// val something = maybeFoo map {_.toString} getOrElse 0
  	// println(something)
  	Ok("Hello")
  }

  def register = Action{
  	Ok(views.html.register())
  }

  def registerSubmit = Action{
    implicit request =>
//  val userData = request.body

     Redirect(routes.Authentication.register)
//    println(userData._1.email)
    Ok("hello")
  }

  def forgotPassword = Action{
  	Ok(views.html.forgot_password())
  }

}