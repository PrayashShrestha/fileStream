package com.learning.backend.respository

import zio._
import io.getquill._

trait UserRepository {
  def register(user: User): IO[AppError, Unit]
  def findByUsername(username: String): IO[AppError, Option[User]]
}

object UserRepository {
  val live: ZLayer[Has[DataSource], Nothing, UserRepository] = ZLayer.succeed(
    new UserRepository {
      val ctx = new H2ZioJdbcContext(SnakeCase)
      import ctx._

      def register(user: User): IO[AppError, Unit] = ctx
        .run {
          quote {
            query[User].insert(lift(user))
          }
        }
        .mapError(e => ServerError(e.getMessage))

      def findByUsername(username: String): IO[AppError, Option[User]] = ctx
        .run {
          quote {
            query[User].filter(_.username == lift(username))
          }
        }
        .map(_.headOption)
        .mapError(e => ServerError(e.getMessage))
    }
  )
}
