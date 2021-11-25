package com.mobimeo.techrt.tapir.api

import cats.implicits.catsSyntaxOptionId
import com.mobimeo.techrt.tapir.domain.{
  BadWeatherError,
  BaseError,
  MorningError,
  NoCaffeineError,
  TooEarlyError
}
import sttp.model.StatusCode
import sttp.tapir.SchemaType.SProductField
import sttp.tapir.json.circe.TapirJsonCirce
import sttp.tapir.{EndpointOutput, FieldName, Schema, SchemaType, Tapir}

import scala.reflect.ClassTag

trait ErrorMapping[E <: BaseError] {
  val statusCode: StatusCode
  val errorCode: E#Code
  val description: String
}

object ErrorMapping {
  def apply[E <: BaseError](
      status: StatusCode,
      desc: String
  )(implicit code: ValueOf[E#Code]): ErrorMapping[E] =
    new ErrorMapping[E] {
      override val errorCode: E#Code      = code.value
      override val statusCode: StatusCode = status
      override val description: String    = desc
    }
}

object Mappings extends Tapir with TapirJsonCirce {

  implicit def schemaForMorningError[E <: BaseError]: Schema[E] =
    Schema(
      SchemaType.SProduct(
        List(
          SProductField(
            FieldName("message"),
            Schema.schemaForString,
            _.message.some
          ),
          SProductField(FieldName("code"), Schema.schemaForString, _.code.some)
        )
      )
    )

  def allow[E <: BaseError: ErrorMapping: ClassTag]
      : EndpointOutput.OneOfVariant[BaseError] = {
    val mapping = implicitly[ErrorMapping[E]]
    oneOfVariantValueMatcher(
      mapping.statusCode,
      jsonBody[BaseError]
        .description(mapping.description)
        .example(new MorningError(mapping.errorCode, mapping.description) {})
    ) { case _: E =>
      true
    }
  }

  implicit val caffeineErrorMapping: ErrorMapping[NoCaffeineError.type] =
    ErrorMapping(
      StatusCode.InternalServerError,
      "Someone forgot to give the server coffee."
    )

  implicit val badWeatherErrorMapping: ErrorMapping[BadWeatherError.type] =
    ErrorMapping(
      StatusCode.PreconditionFailed,
      "It's raining."
    )

  implicit val tooEarlyErrorMapping: ErrorMapping[TooEarlyError] =
    ErrorMapping(
      StatusCode.NotFound,
      "It's so early the server hasn't even shown up yet."
    )
}
