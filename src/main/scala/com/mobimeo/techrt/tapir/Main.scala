package com.mobimeo.techrt.tapir

import cats.effect.{ExitCode, IO, IOApp}
import com.mobimeo.techrt.tapir.server.GoodMorningRoutes
import org.http4s.HttpRoutes
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.ServerBuilder
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {
  val routes: HttpRoutes[IO] = new GoodMorningRoutes[IO]().routes
  val server : ServerBuilder[IO] = BlazeServerBuilder[IO](global).bindHttp(8080, "0.0.0.0")
    .withHttpApp(routes.orNotFound)

  override def run(args: List[String]): IO[ExitCode] = server.resource.use(_ => {
    println("Server up and running.")
    IO.never
  })
}
