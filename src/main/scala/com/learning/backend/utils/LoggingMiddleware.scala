package com.learning.backend.utils

import zio._
import zio.http._
import zio.logging._
import zio.logging.slf4j._

object LoggingMiddleware {
  def withLogging[R <: Logging](httpApp: Routes[R, Throwable]): Routes[R, Throwable] = {
    HttpApp.fromFunctionM { req =>
      for {
        _        <- log.info(s"Request: ${req.method} ${req.url}")
        response <- httpApp(req).either
        _        <- response match {
                      case Right(res) => log.info(s"Response: ${res.status}")
                      case Left(err)  => log.error(s"Error: ${err.getMessage}")
                    }
      } yield response.foldM(ZIO.fail, ZIO.succeed)
    }
  }
}
