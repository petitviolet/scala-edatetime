package net.petitviolet.time

import java.time.format.DateTimeFormatter
import java.time.temporal.{ ChronoUnit, TemporalUnit }
import java.time.{ Duration => _, _ }
import java.util.Locale

import scala.concurrent.duration.Duration

class EDateTime private (val value: ZonedDateTime) extends Ordered[EDateTime] {
  import EDateTime.ZonedDateTimeHelper
  import GlobalEDateTimeSettings.defaultZoneId
  import TimeOps._

  override def compare(that: EDateTime): Int =
    value.compareTo(that.value)

  lazy val epochMillis: EpochMilliseconds = value.epochMilli

  def +(duration: Duration): EDateTime = {
    EDateTime(value.plus(duration.toMillis, ChronoUnit.MILLIS))
  }

  def -(duration: Duration): EDateTime = {
    EDateTime(value.minus(duration.toMillis, ChronoUnit.MILLIS))
  }

  /**
   * "20161223"
   */
  def `yyyy-MM-dd`(implicit zoneId: ZoneId = defaultZoneId): String = {
    value.format(EDateTime.`yyyy-MM-dd`.withZone(zoneId))
  }

  /**
   * "2016-12-23 14:15:33"
   */
  def `yyyy-MM-dd HH:mm:ss`(implicit zoneId: ZoneId = defaultZoneId): String = {
    value.format(EDateTime.`yyyy-MM-dd HH:mm:ss`.withZone(zoneId))
  }

  def format(formatter: DateTimeFormatter): String =
    value.format(formatter)

  override lazy val toString: String = {
    s"EDateTime($value)"
  }

  def asDate: EDate = EDate.apply(value.toLocalDate)
}

object EDateTime {
  import GlobalEDateTimeSettings._

  def apply(value: ZonedDateTime): EDateTime =
    new EDateTime(value)

  def apply(value: LocalDateTime)(implicit zoneId: ZoneId = defaultZoneId): EDateTime = {
    apply(value.atZone(zoneId))
  }

  def now()(implicit zoneId: ZoneId = defaultZoneId) =
    EDateTime(ZonedDateTime.now(zoneId))

  /**
   * [[EpochMilliseconds]]から[[EDateTime]]を作成する
   * @param milliseconds
   * @return
   */
  def fromEpochMilli(
    milliseconds: EpochMilliseconds
  )(implicit zoneId: ZoneId = defaultZoneId): EDateTime = {
    val dateTime = ZonedDateTimeHelper.fromEpochMillis(milliseconds)(zoneId)
    EDateTime(dateTime)
  }

  /**
   * yyyy-MM-dd(e.g. 2017-03-15)から[[EDateTime]]を作成する
   * @param value yyyy-MMdd形式の日付
   * @return
   */
  def `from-yyyy-MM-dd`(value: String)(implicit zoneId: ZoneId = defaultZoneId): EDateTime = {
    require(value.length == 10, s"invalid date expression, $value length is not 10(YYYY-MM-DD)")

    val yyyy = value.substring(0, 4).toInt
    val mm = value.substring(5, 7).toInt
    val dd = value.substring(8, 10).toInt
    val date = ZonedDateTime.of(yyyy, mm, dd, 0, 0, 0, 0, zoneId)
    EDateTime(date)
  }

  def `from_yyyy-MM-dd HH:mm:ss`(value: String): EDateTime = {
    apply(LocalDateTime.parse(value, `yyyy-MM-dd HH:mm:ss`))
  }

  private lazy val `yyyy-MM-dd`: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd", defaultLocale)

  private lazy val `yyyy-MM-dd HH:mm:ss`: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", defaultLocale)

  // 2011/01/01 00:00:00Z+00:00
  lazy val `2011/01/01 00:00:00UTC`: EDateTime =
    EDateTime(ZonedDateTime.of(2011, 1, 1, 0, 0, 0, 0, ZoneId.of("GMT")))

  private implicit class ZonedDateTimeHelper(val zonedDateTime: ZonedDateTime) extends AnyVal {

    def epochMilli: EpochMilliseconds =
      ZonedDateTimeHelper.toEpochMillis(zonedDateTime)
  }

  private object ZonedDateTimeHelper {

    // epoch milliまわりはUTCで計算する
    def fromEpochMillis(
      epochMilliseconds: EpochMilliseconds
    )(implicit zoneId: ZoneId = GlobalEDateTimeSettings.defaultZoneId): ZonedDateTime =
      java.time.Instant
        .ofEpochMilli(epochMilliseconds.value)
        .atZone(zoneId)

    def toEpochMillis(localDateTime: ZonedDateTime): EpochMilliseconds =
      EpochMilliseconds(localDateTime.toInstant.toEpochMilli)
  }
}
