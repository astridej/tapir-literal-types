package com.mobimeo.techrt.tapir.domain

import io.circe.syntax.KeyOps
import io.circe.{Decoder, Encoder, Json}

import java.time.LocalTime

// This extra class means we don't have to
// drag an extra generic parameter everywhere.
// You can also adapt this if you ever need to return
// an error code not known at compile time.
sealed abstract class BaseError(val message: String)
    extends Throwable(message) {
  type Code <: String with Singleton
  val code: Code
}

abstract class MorningError[S <: String with Singleton](
    override val code: S,
    override val message: String
) extends BaseError(message) {
  override type Code = S
}

object BaseError {
  implicit def encoder[E <: BaseError]: Encoder[E] =
    Encoder.instance(error =>
      Json.obj(
        "code"    := error.code.toString, // shut up circe
        "message" := error.message
      )
    )

  implicit def decoder[E <: BaseError]: Decoder[E] = Decoder.failedWithMessage(
    "Only here for tapir, shouldn't need to decode errors"
  )
}

case object BadWeatherError
    extends MorningError(
      "BAD_WEATHER",
      "Cannot say good morning: morning is bad due to bad weather"
    )

object NoCaffeineError
    extends MorningError(
      "NO_CAFFEINE",
      "Greeting failed due to lack of caffeine"
    )

case class TooEarlyError(time: LocalTime)
    extends MorningError(
      "TOO_EARLY",
      s"Greeting failed as ${time.toString} is not morning yet."
    )
