package net.petitviolet.edatetime

import java.time._
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import org.scalactic.anyvals.PosInt
import org.scalatest.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.prop.{ CommonGenerators, Generator, GeneratorDrivenPropertyChecks }

import scala.concurrent.duration._

class EDateTimeTest extends AnyFlatSpec with Matchers with GeneratorDrivenPropertyChecks {
  implicit val localDateTimeGen: Generator[LocalDateTime] = {
    // https://github.com/47deg/scalacheck-toolbox/blob/c9e97ed2980519e5d3df43eac1f603930d656f00/datetime/src/main/scala/com/fortysevendeg/scalacheck/datetime/jdk8/GenJdk8.scala#L31
    for {
      year <- CommonGenerators.intsBetween(-3000, 3000)
      month <- CommonGenerators.intsBetween(1, 12)
      maxDaysInMonth = Month.of(month).length(Year.of(year).isLeap)
      dayOfMonth <- CommonGenerators.intsBetween(1, maxDaysInMonth)
      hour <- CommonGenerators.intsBetween(0, 23)
      minute <- CommonGenerators.intsBetween(0, 59)
      second <- CommonGenerators.intsBetween(0, 59)
      nanoOfSecond <- CommonGenerators.intsBetween(0, 999999999)
    } yield {
      LocalDateTime
        .of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond)
    }
  }

  implicit val zonedDateTimeGen: Generator[ZonedDateTime] = {
    import scala.jdk.CollectionConverters._
    for {
      datetime <- localDateTimeGen
      a :: b :: rest = ZoneId.getAvailableZoneIds.asScala.toList
      zoneIdStr <- CommonGenerators.specificValues(a, b, rest: _*)
      zoneId = ZoneId.of(zoneIdStr)
    } yield {
      ZonedDateTime.of(datetime, zoneId)
    }
  }

  implicit val finiteDurationGen: Generator[FiniteDuration] = {
    for {
      long <- CommonGenerators.posLongValues
    } yield {
      FiniteDuration(long, TimeUnit.NANOSECONDS)
    }
  }

  behavior of "EDateTime.apply"

  it should "create EDateTime instance from ZonedDateTime" in {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    forAll { zdt: ZonedDateTime =>
      val edatetime = EDateTime.apply(zdt)
      edatetime.value shouldBe zdt
      edatetime.`yyyy-MM-dd HH:mm:ss`(zdt.getZone) shouldBe zdt.format(format)
      edatetime.epochMillis.value shouldBe zdt.toInstant.toEpochMilli
    }
  }

  it should "create EDateTime instance from LocalDateTime" in {
    forAll { ldt: LocalDateTime =>
      implicit val zoneId = ZoneIds.UTC
      val edatetime = EDateTime.apply(ldt)
      edatetime.value.toLocalDateTime shouldBe ldt
      edatetime.`yyyy-MM-dd HH:mm:ss` shouldBe {
        ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      }
    }
  }

  behavior of "EDateTime operators"

  it should "+, -" in {
    forAll { (zdt: ZonedDateTime, duration: FiniteDuration) =>
      val edt = EDateTime(zdt)
      (edt + duration).value shouldBe edt.value.plusNanos(duration.toNanos)
      (edt - duration).value shouldBe edt.value.minusNanos(duration.toNanos)
      (edt + duration) should be > edt
      (edt - duration) should be < edt
      ((edt + duration) - duration).value shouldBe edt.value
    }
  }

  it should "to with step" in {
    forAll(zonedDateTimeGen, finiteDurationGen, CommonGenerators.intsBetween(1, 10000)) {
      (zdt: ZonedDateTime, step: FiniteDuration, mul: Int) =>
        val edt = EDateTime(zdt)
        val limit = (0 to mul).foldLeft(edt) { (acc, _) =>
          acc + step
        }
        edt.to(limit, step).size shouldBe mul
    }
  }
}
