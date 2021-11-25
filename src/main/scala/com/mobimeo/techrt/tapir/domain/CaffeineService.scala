package com.mobimeo.techrt.tapir.domain

import cats.{Applicative, MonadThrow}

trait CaffeineService[F[_]] {
  def validateCaffeinated: F[Unit]
}

class ArgumentAwareCaffeineService[F[_]: MonadThrow](args: List[String])
    extends CaffeineService[F] {
  override def validateCaffeinated: F[Unit] =
    if (args.contains("coffee") || args.contains("tea"))
      Applicative[F].unit
    else
      MonadThrow[F].raiseError(NoCaffeineError)
}
