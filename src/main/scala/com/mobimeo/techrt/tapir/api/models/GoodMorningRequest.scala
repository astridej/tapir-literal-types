package com.mobimeo.techrt.tapir.api.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class GoodMorningRequest(name: String)

object GoodMorningRequest {
  implicit val codec: Codec[GoodMorningRequest] = deriveCodec
}
