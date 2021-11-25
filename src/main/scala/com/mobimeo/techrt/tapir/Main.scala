package com.mobimeo.techrt.tapir

import cats.effect.{ExitCode, IO, IOApp}
import com.mobimeo.techrt.tapir.api.OpenAPISupport
import com.mobimeo.techrt.tapir.domain.{
  ArgumentAwareCaffeineService,
  DeviceTimeService,
  PessimisticWeatherService
}
import com.mobimeo.techrt.tapir.server.GoodMorningRoutes
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.ServerBuilder
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val weatherService  = new PessimisticWeatherService[IO]()
    val caffeineService = new ArgumentAwareCaffeineService[IO](args)
    val timeService     = new DeviceTimeService[IO]()
    val endpoints = new GoodMorningRoutes[IO](
      weatherService,
      caffeineService,
      timeService
    ).endpoints
    val swaggerRoutes = SwaggerInterpreter(prefix = List("swagger-ui"))
      .fromServerEndpoints(endpoints, OpenAPISupport.info)
    val redocRoutes = RedocInterpreter(prefix = List("redoc-ui"))
      .fromServerEndpoints(endpoints, OpenAPISupport.info)
    val routes: HttpRoutes[IO] =
      Http4sServerInterpreter[IO]().toRoutes(
        swaggerRoutes ++ redocRoutes ++ endpoints
      )
    val server: ServerBuilder[IO] = BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(routes.orNotFound)

    server.resource.use { _ =>
      println("Server up and running.")
      IO.never
    }
  }
}
