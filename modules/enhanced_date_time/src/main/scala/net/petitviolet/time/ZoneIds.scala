package net.petitviolet.time

import java.time.ZoneId

// alias for java.time.ZoneId
object ZoneIds {
  val UTC: ZoneId = of("UTC")
  val GMT: ZoneId = UTC
  val default: ZoneId = ZoneId.systemDefault()

  /**
   * @see [[java.time.ZoneId.getAvailableZoneIds]]
   */
  val `Asia/Tokyo`: ZoneId = of("Asia/Tokyo")

  def of(zoneId: String): ZoneId = ZoneId.of(zoneId)
}
