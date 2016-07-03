package models

import java.sql.Timestamp

case class Cat(name: String, color: String)


case class User(email: String, password: String, created_at: Timestamp, updated_at: Timestamp)

case class UserProfile(firstname: String, lastname: String, gender: Int, user_id: Long)
