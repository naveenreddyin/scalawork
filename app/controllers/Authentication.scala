package controllers

import java.sql.Timestamp
import java.util.{Calendar, Date}
import javax.inject._

import scala.util.{Failure, Success}
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.{Cat, User, UserProfile}
import dao.{CatDAO, UsersDAO}

import scala.concurrent.Await
import scala.concurrent.duration._



/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class Authentication @Inject() (userDao: UsersDAO) extends Controller {
  val date = new Date()
  val currentTimestamp= new Timestamp(date.getTime());
  val registerForm = Form(
    tuple(
          "user" -> mapping(
            "uid" -> optional(number),
            "email" -> email,
            "password" -> nonEmptyText,
            "created_at" -> ignored(currentTimestamp),
          "updated_at" -> ignored(currentTimestamp)
        )  (User.apply)(User.unapply),
        "profile" -> mapping(
          "firstname"->nonEmptyText,
          "lastname"->nonEmptyText,
          "gender" -> ignored(0),
          "user_id" -> ignored(0L)
        )(UserProfile.apply)(UserProfile.unapply))
    )

  val loginForm = Form(
    tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying("Wrong username or password!", fields => fields match{
      case (username, password) => {
        val query = userDao.authenticate(username, password)
//        var flag: Boolean = false
//        query onComplete  {
//          case Success(Some(user)) => {
//            println("Success "+ user.email+ " "+query.map(_.isDefined))
//
//            flag = true
//          }
//          case Success(None) => {
//            println("User couldnt be found.")
//            flag = false
//          }
//          case Failure(_) => {
//            println("Failed ")
//            flag = false
//
//          }
//        }
//        query onSuccess{
//          case i => println(s"Result: $i "+query.map(_.isDefined))
//        }
//        println(flag)
        val result = Await.result(query.map(_.isDefined), 30 seconds)
        result
      }
    })
  )


  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def login = Action {
    Ok(views.html.login(loginForm))
  }

  def loginSubmit = Action {
  	implicit request => 
  	// val maybeFoo = request.body.asFormUrlEncoded.get("password").lift(0) // returns an Option[String]
  	// val something = maybeFoo map {_.toString} getOrElse 0
  	// println(something)
  	loginForm.bindFromRequest.fold(
      hasErrors => {
        println(hasErrors)

        BadRequest(views.html.login(hasErrors))
      },
      user => {

        Redirect(routes.Authentication.login())
      }
    )
  }

  def register = Action{
  	Ok(views.html.register(registerForm))
  }

  def registerSubmit = Action{
    implicit request =>
    registerForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        println(formWithErrors)
        BadRequest(views.html.register(formWithErrors))
      },
      userData => {
        /* binding success, you get the actual value. */
        println(userData)
        val f = userDao.insert(userData._1)
        f.onSuccess{
          case s =>println(s"Restult: $s")
        }
        f.onFailure{
          case e => println(s"failed $e")
        }
        Redirect(routes.Authentication.login())
      }
    )
  }

  def forgotPassword = Action{
  	Ok(views.html.forgot_password())
  }

}