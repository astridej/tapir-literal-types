package com.mobimeo.techrt.tapir.domain

import cats.effect.kernel.Sync
import cats.implicits._

import java.time.{LocalTime, ZoneId}

trait TimeService[F[_]] {
  def validateIsMorning: F[Unit]
}

class DeviceTimeService[F[_]: Sync] extends TimeService[F] {
  private val startOfMorning = LocalTime.of(8, 0, 0)

  override def validateIsMorning: F[Unit] = Sync[F]
    .delay {
      LocalTime.now(ZoneId.systemDefault())
    }
    .flatMap { now =>
      if (now.isBefore(startOfMorning))
        Sync[F].raiseError(TooEarlyError(now))
      else
        Sync[F].unit
    }
}
