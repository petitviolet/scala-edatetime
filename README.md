# edatetime - enhanced date time library for Scala

[![MavenCentral](https://maven-badges.herokuapp.com/maven-central/net.petitviolet/edatetime_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.petitviolet/edatetime_2.12)

## setup

in <project root>/build.sbt.

```scala
libraryDependencies += "net.petitviolet % "edatetime" % "<version>"
```

## Usage

```scala
import net.petitviolet.edatetime._

// if you needed. default is ZoneId.systemDefault
GlobalEDateTimeSettings.zoneId = ZoneId.of("Asia/Tokyo")

println(s"${EDateTime.now()}")
println(s"${EDateTime.now()(ZoneId.of("America/New_York")}") // or provide zoneId explicitly
```

- more samples

```scala
@ import $ivy.`net.petitviolet::edatetime:0.3.0`; import net.petitviolet.edatetime._; 
import $ivy.$                                          ;
import net.petitviolet.edatetime._;

@ EDateTime.now()
res1: EDateTime = EDateTime(2019-03-03T23:05:39.835801+09:00[Asia/Tokyo])

@ EDateTime.now()(ZoneIds.UTC)
res2: EDateTime = EDateTime(2019-03-03T14:05:48.515310Z[UTC])

@ EDateTime.now()(ZoneIds.UTC).`yyyy-MM-dd HH:mm:ss`
res3: String = "2019-03-03 23:06:09"

@ EDateTime.now()(ZoneIds.UTC).`yyyy-MM-dd HH:mm:ss`(ZoneIds.UTC)
res4: String = "2019-03-03 14:06:24"

@ implicit val zoneId = ZoneIds.UTC
zoneId: ZoneId = UTC

@ EDateTime.now().`yyyy-MM-dd HH:mm:ss`
res6: String = "2019-03-03 14:07:00"

@ import $ivy.`net.petitviolet::cached-edatetime:0.3.0`; import net.petitviolet.edatetime.cache._
import $ivy.$                                                ;
import net.petitviolet.edatetime.cache._

@ (1 to 10) foreach { _ =>
    val now = EDateTime.now()
    val cache = CachedEDateTime.now()
    println(s"${now} : ${cache}, diff: ${now diff cache}")
    Thread.sleep(200L)
  }
EDateTime(2019-03-04T13:36:37.342389+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.112854+09:00[Asia/Tokyo]), diff: 230 milliseconds
EDateTime(2019-03-04T13:36:37.549710+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.112854+09:00[Asia/Tokyo]), diff: 437 milliseconds
EDateTime(2019-03-04T13:36:37.753297+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.112854+09:00[Asia/Tokyo]), diff: 641 milliseconds
EDateTime(2019-03-04T13:36:37.957288+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.913841+09:00[Asia/Tokyo]), diff: 44 milliseconds
EDateTime(2019-03-04T13:36:38.161705+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.913841+09:00[Asia/Tokyo]), diff: 248 milliseconds
EDateTime(2019-03-04T13:36:38.364396+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.913841+09:00[Asia/Tokyo]), diff: 451 milliseconds
EDateTime(2019-03-04T13:36:38.568921+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:37.913841+09:00[Asia/Tokyo]), diff: 655 milliseconds
EDateTime(2019-03-04T13:36:38.771602+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:38.716515+09:00[Asia/Tokyo]), diff: 55 milliseconds
EDateTime(2019-03-04T13:36:38.976066+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:38.716515+09:00[Asia/Tokyo]), diff: 260 milliseconds
EDateTime(2019-03-04T13:36:39.180846+09:00[Asia/Tokyo]) : EDateTime(2019-03-04T13:36:38.716515+09:00[Asia/Tokyo]), diff: 464 milliseconds

@ import scala.concurrent.duration.Duration._
import scala.concurrent.duration.Duration._

@ EDateTime.now() - 100.days; EDateTime.now(); EDateTime.now() + 100.days // +/- operator
res7_0: EDateTime = EDateTime(2018-11-23T14:11:09.612408Z[UTC])
res7_1: EDateTime = EDateTime(2019-03-03T14:11:09.612480Z[UTC])
res7_2: EDateTime = EDateTime(2019-06-11T14:11:09.612485Z[UTC])

@ (EDateTime.now() - 100.days) < EDateTime.now() // compare
res8: Boolean = true

@ EDateTime.now()(ZoneIds.UTC).epochMillis; EDateTime.now()(ZoneIds.`Asia/Tokyo`)).epochMillis // epoch milliseconds
res9_0: EpochMilliseconds = EpochMilliseconds(1551622408081L)
res9_1: EpochMilliseconds = EpochMilliseconds(1551622408081L)

@ EDateTime.fromEpochMilli(EpochMilliseconds(1551622408081L)); EDateTime.fromEpochMilli(EpochMilliseconds(1551622408081L))(ZoneIds.`Asia/Tokyo`))
res10_0: EDateTime = EDateTime(2019-03-03T14:13:28.081Z[UTC])
res10_1: EDateTime = EDateTime(2019-03-03T23:13:28.081+09:00[Asia/Tokyo])
```

## cached date time

Also provide `enhanced_date_time_cache` library, caching `EDateTime` instance and reload it by 800ms.  
I propose to use this when your project needs datetime frequently but not needs high-precision.

```scala
libraryDependencies += "net.petitviolet" % "cached-edatetime" % "<version>"
```

### Usage

```scala
import net.petitviolet.edatetime.cache.CachedDateTime

println(s"before = ${CachedDateTime.now()}")
Thread.sleep(3000L)
println(s"after = ${CachedDateTime.now()}")
```

## LICENSE

[Apache-2.0](https://github.com/petitviolet/scala-edatetime/blob/master/LICENSE)
