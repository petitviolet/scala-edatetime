package net.petitviolet.time

import java.time.{ Clock, Instant, ZoneId, ZoneOffset }
import java.util.Locale

object GlobalEDateTimeSettings {
  @volatile var defaultZoneId: ZoneId = ZoneId.systemDefault()
  @volatile var defaultLocale: Locale = Locale.getDefault

  def zoneOffset: ZoneOffset =
    defaultZoneId.getRules.getOffset(Instant.now(Clock.system(defaultZoneId)))
}
