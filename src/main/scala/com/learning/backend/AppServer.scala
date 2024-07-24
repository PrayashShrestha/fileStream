package com.learning.backend

import zio.*
import zio.http.*
import zio.logging.LogFormat
import zio.logging.backend.SLF4J
import zio.logging.{LogFormat, console}

object AppServer extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    SLF4J.slf4j(LogFormat.colored)

  val loggedApp = withLogging(httpApp)

  def run: ZIO[Any, Throwable, Nothing] = {
    val httpApp = combineRoutes(
      AuthRoutes.routes,
      FileRoutes.routes
    )

    Server
      .serve(loggedApp)
      .provide(
        ZLayer.fromZIO(Ref.make(0)),
        Server.defaultWithPort(8080),
        AppConfig.live,
        UserRepository.live,
        FileRepository.live,
        AuthService.live,
        FileService.live
      )
      .orDie
  }

}

//object Main extends ZIOAppDefault {
//  val parentEffect = for {
//    _          <- ZIO.succeed(println("Parent fiber starting"))
//    childFiber <- ZIO.succeed(println("Forking child fiber")).fork
//    _          <- ZIO.sleep(5.seconds)
//    _          <- ZIO.succeed(println("Parent fiber ending"))
//  } yield ()
//
//  val daemonEffect = for {
//    _ <- ZIO.succeed(println("Daemon fiber starting"))
//    _ <- ZIO.succeed(println("Forking daemon fiber")).forkDaemon
//    _ <- ZIO.sleep(2.seconds)
//    _ <- ZIO.succeed(println("Daemon fiber ending"))
//  } yield ()
//
//  override def run = for {
//    ref   <- Ref.make(0)
//    _     <- ZIO.foreachPar(1 to 100)(_ => ref.update(_ + 1)).fork // Concurrently increment the counter
//    _     <- ZIO.sleep(1.second)
//    value <- ref.get
//    _     <- ZIO.succeed(println(s"Final counter value: $value"))
//  } yield ()
//}
