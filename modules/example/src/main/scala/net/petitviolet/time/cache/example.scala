package net.petitviolet.time.cache

import java.time.ZoneId

import net.petitviolet.time.GlobalEDateTimeSettings

object example extends App {
  println("=====================")
  CachedDateTime.init()
  println(s"now = ${CachedDateTime.now()}")
  GlobalEDateTimeSettings.zoneId = ZoneId.of("America/New_York")
  Thread.sleep(3000L)
  println(s"now = ${CachedDateTime.now()}")
  println("=====================")
  CachedDateTime.shutdown()
}
