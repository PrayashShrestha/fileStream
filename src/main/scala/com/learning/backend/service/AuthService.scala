package com.learning.backend.service

import com.example.repository._
import zio._

trait AuthService {
  def register(user: User): IO[AppError, Unit]
  def login(username: String, password: String): IO[AppError, User]
}

object AuthService {
  val live: ZLayer[UserRepository, Nothing, AuthService] = ZLayer.fromFunction { repo: UserRepository =>
    new AuthService {
      def register(user: User): IO[AppError, Unit] = repo.register(user)

      def login(username: String, password: String): IO[AppError, User] =
        for {
          userOpt <- repo.findByUsername(username)
          user    <- ZIO.fromOption(userOpt).orElseFail(InvalidCredentials("Invalid username or password"))
          _       <- ZIO.fail(InvalidCredentials("Invalid password")).unless(user.password == password)
        } yield user
    }
  }
}
