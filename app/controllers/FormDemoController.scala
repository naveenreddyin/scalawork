package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.libs.concurrent.Execution.Implicits.defaultContext


import dao.CatDAO
import models.Cat



case class UserData(name: String, age: Int)


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class FormDemoController @Inject() (catDao: CatDAO)extends Controller{
val userForm = Form(
  mapping(
    "name" -> text,
    "age" -> number
  )(UserData.apply)(UserData.unapply)
)
  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.form(userForm))
  }


  def create = Action { implicit request =>
    userForm.bindFromRequest.fold(
  formWithErrors => {
    // binding failure, you retrieve the form containing errors:
    BadRequest(views.html.form(formWithErrors))
  },
  userData => {
    /* binding success, you get the actual value. */

    // val newUser = models.User(userData.name, userData.age)
    // val id = models.User.create(newUser)
    Ok(userData.name)
  }
)
  }

  val catForm = Form(
    mapping(
      "name" -> text(),
      "color" -> text()
    )(Cat.apply)(Cat.unapply)
  )


  def insertCat = Action.async { implicit request =>
    val cat: Cat = catForm.bindFromRequest.get
    catDao.insert(cat).map(_ => Redirect(routes.FormDemoController.catIndex))
  }

  def catIndex = Action.async {
    catDao.all().map {case (cats) => Ok(views.html.cat(cats)) }
  }
  

}


