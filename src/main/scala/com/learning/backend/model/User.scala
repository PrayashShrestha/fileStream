package com.learning.backend.model

import zio.json._

case class User(id: String, username: String, password: String)

object User {
  implicit val decoder: JsonDecoder[User]  = DeriveJsonDecoder.gen[User]
  implicit val encodeer: JsonEncoder[User] = DeriveJsonEncoder.gen[User]
}
