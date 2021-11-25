package com.mobimeo.techrt.tapir.api.models

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec

case class GoodMorningResponse(message: String)

object GoodMorningResponse {
  implicit val codec: Codec[GoodMorningResponse] = deriveCodec

  def forName(name: String): GoodMorningResponse = GoodMorningResponse(
    s"Good morning, $name!"
  )
}
