package models

import java.sql.Timestamp

case class Cat(name: String, color: String)


case class User(uid: Option[Int], email: String, password: String, created_at: Timestamp, updated_at: Timestamp)

case class UserProfile(upid: Option[Int], firstname: String, lastname: String, gender: Int, user_id: Int)
