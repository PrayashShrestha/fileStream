package com.learning.backend

import zio.*
import zio.http.*
import zio.logging.LogFormat
import zio.logging.backend.SLF4J

object AppServer extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    SLF4J.slf4j(LogFormat.colored)

  private val routes = Routes(
    Method.GET / Root    -> handler(Response.text("Greetings at your service")),
    Method.GET / "greet" -> handler { (req: Request) =>
      val name = req.queryParamToOrElse("name", "World")
      Response.text(s"Hello $name!")
    }
  )

  def run: ZIO[Any, Throwable, Nothing] =
    Server
      .serve(routes)
      .provide(
        ZLayer.succeed(
          Server.Config.default.port(port = 8080)
        ),
        Server.live
      )
      .orDie
}

object Main extends ZIOAppDefault {
  val parentEffect = for {
    _          <- ZIO.succeed(println("Parent fiber starting"))
    childFiber <- ZIO.succeed(println("Forking child fiber")).fork
    _          <- ZIO.sleep(5.seconds)
    _          <- ZIO.succeed(println("Parent fiber ending"))
  } yield ()

  val daemonEffect = for {
    _ <- ZIO.succeed(println("Daemon fiber starting"))
    _ <- ZIO.succeed(println("Forking daemon fiber")).forkDaemon
    _ <- ZIO.sleep(2.seconds)
    _ <- ZIO.succeed(println("Daemon fiber ending"))
  } yield ()

  override def run = for {
    ref   <- Ref.make(0)
    _     <- ZIO.foreachPar(1 to 100)(_ => ref.update(_ + 1)).fork // Concurrently increment the counter
    _     <- ZIO.sleep(1.second)
    value <- ref.get
    _     <- ZIO.succeed(println(s"Final counter value: $value"))
  } yield ()
}
