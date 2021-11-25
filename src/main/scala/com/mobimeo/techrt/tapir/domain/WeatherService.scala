package com.mobimeo.techrt.tapir.domain

import cats.{ApplicativeError, ApplicativeThrow}

trait WeatherService[F[_]] {
  def validateWeather: F[Unit]
}

// It's November.
class PessimisticWeatherService[F[_]: ApplicativeThrow]
    extends WeatherService[F] {
  override def validateWeather: F[Unit] =
    ApplicativeError[F, Throwable].raiseError(BadWeatherError)
}
