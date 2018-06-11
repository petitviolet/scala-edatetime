package net.petitviolet.time.example

import java.time.ZoneId

import net.petitviolet.time.cache.CachedDateTime
import net.petitviolet.time.{EDateTime, GlobalEDateTimeSettings}

object example extends App {
  println("=====================")
  println(s"${EDateTime.now()}")
  CachedDateTime.init()
  println(s"now = ${CachedDateTime.now()}")
  GlobalEDateTimeSettings.zoneId = ZoneId.of("America/New_York")
  Thread.sleep(3000L)
  println(s"now = ${CachedDateTime.now()}")
  println(s"${EDateTime.now()}")
  println("=====================")
  CachedDateTime.shutdown()
}
