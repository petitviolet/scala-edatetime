package net.petitviolet.edatetime

import java.time.{ ZoneId, ZonedDateTime }

object Implicits {

  def fromEpochMillis(
    epochMilliseconds: EpochMilliseconds
  )(implicit zoneId: ZoneId = GlobalEDateTimeSettings.defaultZoneId): ZonedDateTime = {
    java.time.Instant
      .ofEpochMilli(epochMilliseconds.value)
      .atZone(zoneId)
  }

  def toEpochMillis(zonedDateTime: ZonedDateTime): EpochMilliseconds = {
    EpochMilliseconds(zonedDateTime.toInstant.toEpochMilli)
  }

  implicit class ZonedDateTimeHelper(val zonedDateTime: ZonedDateTime) extends AnyVal {
    def epochMilli: EpochMilliseconds = toEpochMillis(zonedDateTime)
  }
}
