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
