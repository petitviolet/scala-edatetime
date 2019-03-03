package net.petitviolet.time.example

import net.petitviolet.time.cache.CachedEDateTime
import net.petitviolet.time._

object example extends App {
  println("=====================")
  println(s"now(default       ) = ${EDateTime.now()}")
  println(s"now(new york      ) = ${EDateTime.now()(ZoneIds.of("America/New_York"))}")
  println(s"now(default/cached) = ${CachedEDateTime.now()}")

  Thread.sleep(3000L)
  println(s"sleep 3000 milliseconds")
  println(s"now(default/cached) = ${CachedEDateTime.now()}")
  println(s"now(default       ) = ${EDateTime.now()}")

  GlobalEDateTimeSettings.defaultZoneId = ZoneIds.of("America/New_York")
  Thread.sleep(1000L)
  println(s"sleep 1000 milliseconds")
  println(s"now(new york       ) = ${EDateTime.now()}")
  println(s"now(new york/cached) = ${CachedEDateTime.now()}")
  println("=====================")
  CachedEDateTime.shutdown()
  println(s"now(new york/stopped cache) = ${CachedEDateTime.now()}")
}
