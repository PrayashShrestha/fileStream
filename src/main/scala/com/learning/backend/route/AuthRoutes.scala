package com.learning.backend.route

import zio._
import zio.http._
import zio.json._

object AuthRoutes {
  private val prefix = "auth"

  val routes = Http.collectZIO[Request] {
    case req @ Method.POST -> !! / `prefix` / "register" =>
      for {
        body <- req.body.asString
        user <- ZIO.fromEither(body.fromJson[User]).mapError(_ => InvalidInput("Invalid user data"))
        _    <- AuthService.register(user)
      } yield Response.text("User registered successfully")

    case req @ Method.POST -> !! / `prefix` / "login" =>
      for {
        body                <- req.body.asString
        credentials         <-
          ZIO.fromEither(body.fromJson[(String, String)]).mapError(_ => InvalidInput("Invalid credentials"))
        (username, password) = credentials
        user                <- AuthService.login(username, password)
      } yield Response.text(s"Welcome ${user.username}")
  }
}
