package dao

import java.sql.Timestamp

import scala.concurrent.Future
import javax.inject.Inject

import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile
import slick.profile.SqlProfile.ColumnOption.SqlType

class UsersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[Unit] = {
    println("coming inside insert of user dao")
    println(user)
    db.run(Users += user).map { _ => user }
  }

  private class UsersTable(tag: Tag) extends Table[User](tag, "users") {

    def email = column[String]("email")
    def password = column[String]("password")
    def created_at = column[Timestamp]("created_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
    def updated_at = column[Timestamp]("updated_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))

    def * = (email, password, created_at, updated_at) <> (User.tupled, User.unapply _)
  }


  
}