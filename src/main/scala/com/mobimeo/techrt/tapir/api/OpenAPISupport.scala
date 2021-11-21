package com.mobimeo.techrt.tapir.api

import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.json.circe.TapirJsonCirce
import sttp.tapir.openapi.{Info, OpenAPI}
import sttp.tapir.openapi.circe.yaml.TapirOpenAPICirceYaml

trait OpenAPISupport extends TapirJsonCirce with TapirOpenAPICirceYaml with OpenAPIDocsInterpreter {}

object OpenAPISupport extends OpenAPISupport {
  val openApi: OpenAPI = toOpenAPI(
    List(
      GoodMorningEndpoints.`POST /morning`,
      GoodMorningEndpoints.`GET /openapi`,
    ),
    Info("Tech Roundtable Demo Service", "1.0")
  )
}