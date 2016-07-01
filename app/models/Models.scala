package models

case class Cat(name: String, color: String)


case class User(email: String, password: String)

case class UserProfile(firstname: String, lastname: String, gender: Int, user_id: Long)
