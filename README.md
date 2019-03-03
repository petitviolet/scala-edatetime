# scala enhanced date time

[![MavenCentral](https://maven-badges.herokuapp.com/maven-central/net.petitviolet/enhanced_date_time_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.petitviolet/enhanced_date_time_2.12/badge.svg)

## setup

in <project root>/build.sbt.

```scala
libraryDependencies += "net.petitviolet % "enhanced_date_time" % "<version>"
```

## Usage

```scala
import net.petitviolet.time._

// if you needed. default is ZoneId.systemDefault
GlobalEDateTimeSettings.zoneId = ZoneId.of("Asia/Tokyo")

println(s"${EDateTime.now()}")
println(s"${EDateTime.now()(ZoneId.of("America/New_York")}") // or provide zoneId explicitly
```

- more samples

```scala
@ import $ivy.`net.petitviolet::enhanced_date_time:0.2.0`; import net.petitviolet.time._; 
import $ivy.$                                          ;
import net.petitviolet.time._;

@ EDateTime.now()
res1: EDateTime = EDateTime(2019-03-03T23:05:39.835801+09:00[Asia/Tokyo])

@ EDateTime.now()(java.time.ZoneId.of("UTC"))
res2: EDateTime = EDateTime(2019-03-03T14:05:48.515310Z[UTC])

@ EDateTime.now()(java.time.ZoneId.of("UTC")).`yyyy-MM-dd HH:mm:ss`
res3: String = "2019-03-03 23:06:09"

@ EDateTime.now()(java.time.ZoneId.of("UTC")).`yyyy-MM-dd HH:mm:ss`(java.time.ZoneId.of("UTC"))
res4: String = "2019-03-03 14:06:24"

@ implicit val zoneId = java.time.ZoneId.of("UTC")
zoneId: java.time.ZoneId = UTC

@ EDateTime.now().`yyyy-MM-dd HH:mm:ss`
res6: String = "2019-03-03 14:07:00"

@ import $ivy.`net.petitviolet::enhanced_date_time_cache:0.2.0`; import net.petitviolet.time.cache._
import $ivy.$                                                ;
import net.petitviolet.time.cache._

@ (1 to 10) foreach { _ =>
    println(s"${EDateTime.now()} : ${CachedEDateTime.now()}")
    Thread.sleep(200L)
  }
EDateTime(2019-03-03T14:07:19.661515Z[UTC]) : EDateTime(2019-03-03T23:07:19.675589+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:19.882114Z[UTC]) : EDateTime(2019-03-03T23:07:19.675589+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:20.086092Z[UTC]) : EDateTime(2019-03-03T23:07:19.675589+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:20.291306Z[UTC]) : EDateTime(2019-03-03T23:07:19.675589+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:20.493215Z[UTC]) : EDateTime(2019-03-03T23:07:20.475501+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:20.693851Z[UTC]) : EDateTime(2019-03-03T23:07:20.475501+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:20.898141Z[UTC]) : EDateTime(2019-03-03T23:07:20.475501+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:21.101038Z[UTC]) : EDateTime(2019-03-03T23:07:20.475501+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:21.304894Z[UTC]) : EDateTime(2019-03-03T23:07:21.276991+09:00[Asia/Tokyo])
EDateTime(2019-03-03T14:07:21.506776Z[UTC]) : EDateTime(2019-03-03T23:07:21.276991+09:00[Asia/Tokyo])
```

## cached date time

Also provide `enhanced_date_time_cache` library, caching `EDateTime` instance and reload it by 1000ms.  
I propose to use this when your project needs datetime frequently but not needs high-precision.

```scala
libraryDependencies += "net.petitviolet % "enhanced_date_time_cache" % "<version>"
```

### Usage

```scala
import net.petitviolet.time.cache.CachedDateTime
import net.petitviolet.time._

println(s"before = ${CachedDateTime.now()}")
Thread.sleep(3000L)
println(s"after = ${CachedDateTime.now()}")
```

## LICENSE

[Apache-2.0](https://github.com/petitviolet/scala_enhanced_datetime/blob/master/LICENSE)
