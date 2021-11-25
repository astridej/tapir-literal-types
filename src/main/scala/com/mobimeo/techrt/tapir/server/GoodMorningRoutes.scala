package com.mobimeo.techrt.tapir.server

import cats.implicits._
import cats.{Applicative, MonadThrow}
import com.mobimeo.techrt.tapir.api.models.GoodMorningResponse
import com.mobimeo.techrt.tapir.api.{GoodMorningEndpoints, OpenAPISupport}
import com.mobimeo.techrt.tapir.domain.{
  CaffeineService,
  TimeService,
  WeatherService
}
import sttp.tapir
import sttp.tapir.server.ServerEndpoint

import scala.language.postfixOps
import scala.reflect.ClassTag

class GoodMorningRoutes[F[_]: MonadThrow](
    weatherService: WeatherService[F],
    caffeineService: CaffeineService[F],
    timeService: TimeService[F]
) {

  implicit class OurPlainEndpoint[I, O](
      endpoint: tapir.Endpoint[Unit, I, Unit, O, Any]
  ) {
    def withLogic(logic: I => F[O]): ServerEndpoint[Any, F] =
      endpoint.serverLogic(i => logic(i).map[Either[Unit, O]](Right.apply))
  }

  implicit class OurErrorHandlingEndpoint[I, E <: Throwable: ClassTag, O](
      endpoint: tapir.Endpoint[Unit, I, E, O, Any]
  ) {
    def withLogic(logic: I => F[O]): ServerEndpoint[Any, F] =
      endpoint.serverLogic(i => logic(i).attemptNarrow[E])
  }

  val possiblyGoodMorning: ServerEndpoint[Any, F] =
    GoodMorningEndpoints.`POST /morning/v2`.withLogic { request =>
      for {
        _ <- caffeineService.validateCaffeinated
        _ <- timeService.validateIsMorning
        _ <- weatherService.validateWeather
      } yield GoodMorningResponse.forName(request.name)
    }

  val goodMorning: ServerEndpoint[Any, F] =
    GoodMorningEndpoints.`POST /morning`.withLogic { request =>
      Applicative[F].pure(GoodMorningResponse.forName(request.name))
    }

  val openApi: ServerEndpoint[Any, F] =
    GoodMorningEndpoints.`GET /openapi`.withLogic { _ =>
      Applicative[F].pure(OpenAPISupport.openApiYaml)
    }

  val endpoints = List(goodMorning, possiblyGoodMorning, openApi)
}
