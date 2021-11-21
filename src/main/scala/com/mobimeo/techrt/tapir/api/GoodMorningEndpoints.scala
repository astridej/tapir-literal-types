package com.mobimeo.techrt.tapir.api

import com.mobimeo.techrt.tapir.api.models.{GoodMorningRequest, GoodMorningResponse}
import sttp.tapir.{Endpoint, endpoint, stringBody}
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody

import java.nio.charset.StandardCharsets

object GoodMorningEndpoints {
  val `POST /morning`: Endpoint[GoodMorningRequest, Unit, GoodMorningResponse, Any] = endpoint
    .in("morning")
    .post
    .in(jsonBody[GoodMorningRequest])
    .out(jsonBody[GoodMorningResponse])

  val `GET /openapi`: Endpoint[Unit, Unit, String, Any] = endpoint
    .in("openapi")
    .get
    .out(stringBody(StandardCharsets.UTF_8))
}
