package com.mobimeo.techrt.tapir.api

import com.mobimeo.techrt.tapir.api.Mappings._
import com.mobimeo.techrt.tapir.api.models.{
  GoodMorningRequest,
  GoodMorningResponse
}
import com.mobimeo.techrt.tapir.domain.{
  BadWeatherError,
  BaseError,
  NoCaffeineError,
  TooEarlyError
}
import sttp.tapir.generic.Derived
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{Endpoint, Schema, Tapir}

import java.nio.charset.StandardCharsets

object GoodMorningEndpoints extends Tapir {
  implicit val schema: Schema[GoodMorningRequest] =
    implicitly[Derived[Schema[GoodMorningRequest]]].value
      .modify(_.name)(
        _.description("Your name goes here!")
          .encodedExample("Harry Potter")
      )

  val `POST /morning`
      : Endpoint[Unit, GoodMorningRequest, Unit, GoodMorningResponse, Any] =
    endpoint
      .in("morning")
      .post
      .in(jsonBody[GoodMorningRequest])
      .out(jsonBody[GoodMorningResponse])
      .description("Tell it your name to be told good morning in return!")

  val `POST /morning/v2`: Endpoint[
    Unit,
    GoodMorningRequest,
    BaseError,
    GoodMorningResponse,
    Any
  ] = endpoint
    .in("morning")
    .in("v2")
    .post
    .in(jsonBody[GoodMorningRequest])
    .out(jsonBody[GoodMorningResponse])
    .errorOut(
      oneOf(
        allow[BadWeatherError.type],
        allow[TooEarlyError],
        allow[NoCaffeineError.type]
      )
    )
    .description(
      "Tell it your name and _maybe_ be told good morning. But only if it is one."
    )

  val `GET /openapi`: Endpoint[Unit, Unit, Unit, String, Any] =
    endpoint
      .in("openapi")
      .get
      .out(stringBody(StandardCharsets.UTF_8))
      .description("Get this API documentation in OpenAPI YAML format.")
}
