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

import com.github.t3hnar.bcrypt._

class UsersDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  private val Users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(Users.result)

  def insert(user: User): Future[Any] = {
    println("coming inside insert of user dao")
    println(user)
//    insertUP(user)
    val hashPassword = user.password.bcrypt
    val updatedUser = user.copy(password = hashPassword)

    db.run((Users returning Users.map(_.uid)) += updatedUser)
  }
  def insertUP(user: User) = {
    DBIO.seq(
      Users += user

    )
    println(Users.insertStatement)
  }

  def authenticate(username: String, password: String): Future[Seq[User]] = {
//    val query = Users.filter(_.email === username).filter(_.password === password.bcrypt)
//    val action = query.result
//    val result: Future[Seq[User]] = db.run(action)
//    val sql = action.statements.head
//    println(sql)
    val query = Users.filter(u => u.email === username && u.password === password.bcrypt).result
    val f: Future[Seq[User]] = db.run(query)
    f

  }

  private class UsersTable(tag: Tag) extends Table[User](tag, "users") {

    def uid = column[Int]("uid", O.PrimaryKey, O.AutoInc, O.SqlType("INT"))
    def email = column[String]("email")
    def password = column[String]("password")
    def created_at = column[Timestamp]("created_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
    def updated_at = column[Timestamp]("updated_at", SqlType("timestamp not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP"))
    def idx = index("email_UNIQUE", email, unique = true)

    def * = (uid.?, email, password, created_at, updated_at) <> (User.tupled, User.unapply _)
  }


  
}