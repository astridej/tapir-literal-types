package com.mobimeo.techrt.tapir.server

import cats.effect.{Concurrent, ContextShift, Timer}
import cats.implicits._
import com.mobimeo.techrt.tapir.api.{GoodMorningEndpoints, OpenAPISupport}
import com.mobimeo.techrt.tapir.api.models.GoodMorningResponse
import org.http4s.HttpRoutes
import sttp.tapir
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.circe.TapirJsonCirce
import sttp.tapir.openapi.circe.yaml.TapirOpenAPICirceYaml
import sttp.tapir.server.http4s.Http4sServerInterpreter

class GoodMorningRoutes[F[_] : Concurrent : ContextShift : Timer] extends TapirJsonCirce with TapirOpenAPICirceYaml with OpenAPIDocsInterpreter {

  implicit class OurRoute[I, O](endpoint: tapir.Endpoint[I, Unit, O, Any]) {
    def asBasicRoute(logic: I => F[O]): HttpRoutes[F] =
      Http4sServerInterpreter.toRoutes(endpoint)(i => logic(i).map(Right.apply))
  }

  val goodMorning: HttpRoutes[F] = GoodMorningEndpoints.`POST /morning`.asBasicRoute { request =>
    Concurrent[F].pure(GoodMorningResponse(s"Good morning, ${request.name}!"))
  }

  val openApi: HttpRoutes[F] = GoodMorningEndpoints.`GET /openapi`.asBasicRoute { _ =>
    Concurrent[F].pure(OpenAPISupport.openApi.toYaml)
  }

  val routes = goodMorning <+> openApi
}
