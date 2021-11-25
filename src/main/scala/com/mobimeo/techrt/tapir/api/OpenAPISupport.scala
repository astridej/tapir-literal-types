package com.mobimeo.techrt.tapir.api

import cats.Applicative
import cats.effect.IO
import com.mobimeo.techrt.tapir.domain.{
  CaffeineService,
  TimeService,
  WeatherService
}
import com.mobimeo.techrt.tapir.server.GoodMorningRoutes
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.TapirOpenAPICirceYaml
import sttp.tapir.openapi.{Info, OpenAPI}

object OpenAPISupport
    extends OpenAPIDocsInterpreter
    with TapirOpenAPICirceYaml {
  private val stubbedService = new NoopService[IO]()
  val info: Info             = Info("Tech Roundtable Demo Service", "1.0")
  val openApi: OpenAPI = serverEndpointsToOpenAPI(
    new GoodMorningRoutes(
      stubbedService,
      stubbedService,
      stubbedService
    ).endpoints,
    info
  )

  val openApiYaml: String = openApi.toYaml
}

class NoopService[F[_]: Applicative]
    extends CaffeineService[F]
    with TimeService[F]
    with WeatherService[F] {

  override def validateCaffeinated: F[Unit] = Applicative[F].unit

  override def validateIsMorning: F[Unit] = Applicative[F].unit

  override def validateWeather: F[Unit] = Applicative[F].unit
}
