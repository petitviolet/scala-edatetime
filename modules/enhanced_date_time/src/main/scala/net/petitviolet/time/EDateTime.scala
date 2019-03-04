package net.petitviolet.time

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{ Duration => _, _ }

import scala.concurrent.duration.FiniteDuration

/**
 * enhanced DateTime API wrapping java.time.ZonedDateTime.
 *
 * @param value java.time.ZonedDateTime
 */
class EDateTime private (val value: ZonedDateTime) extends Ordered[EDateTime] { self =>
  import EDateTime.ZonedDateTimeHelper
  import GlobalEDateTimeSettings.defaultZoneId

  override def compare(that: EDateTime): Int =
    value.compareTo(that.value)

  /**
   * milliseconds passed from 1970/01/01
   */
  lazy val epochMillis: EpochMilliseconds = value.epochMilli

  /**
   * plus operator
   * @param duration [[scala.concurrent.duration.Duration]]
   * @return [[EDateTime]]
   */
  def +(duration: FiniteDuration): EDateTime = {
    EDateTime(value.plus(duration.toMillis, ChronoUnit.MILLIS))
  }

  /**
   * minus operator
   * @param duration [[scala.concurrent.duration.Duration]]
   * @return [[EDateTime]]
   */
  def -(duration: FiniteDuration): EDateTime = {
    EDateTime(value.minus(duration.toMillis, ChronoUnit.MILLIS))
  }

  def diff(other: EDateTime): FiniteDuration = {
    self.epochMillis diff other.epochMillis
  }

  /**
   * format as 'yyyy-MM-dd' (e.g. "2016-12-23")
   */
  def `yyyy-MM-dd`(implicit zoneId: ZoneId = defaultZoneId): String = {
    value.format(EDateTime.`yyyy-MM-dd`.withZone(zoneId))
  }

  /**
   * format as 'yyyy-MM-dd HH:mm:ss' (e.g. "2016-12-23 14:15:33")
   */
  def `yyyy-MM-dd HH:mm:ss`(implicit zoneId: ZoneId = defaultZoneId): String = {
    value.format(EDateTime.`yyyy-MM-dd HH:mm:ss`.withZone(zoneId))
  }

  /**
   * format as provided format
   * @param formatter [[java.time.format.DateTimeFormatter]]
   * @return formatted String
   */
  def format(formatter: DateTimeFormatter): String =
    value.format(formatter)

  override lazy val toString: String = {
    s"EDateTime($value)"
  }

  def asDate: EDate = EDate.apply(value.toLocalDate)
}

object EDateTime {

  import GlobalEDateTimeSettings._

  /**
   * specified datetime factory for [[EDateTime]]
   *
   * @param value [[java.time.ZonedDateTime]]
   * @return [[EDateTime]]
   */
  def apply(value: ZonedDateTime): EDateTime =
    new EDateTime(value)

  /**
   * specified datetime factory for [[EDateTime]]
   *
   * @param value [[java.time.LocalDateTime]]
   * @param zoneId [[java.time.ZoneId]]
   * @return [[EDateTime]]
   */
  def apply(value: LocalDateTime)(implicit zoneId: ZoneId = defaultZoneId): EDateTime = {
    apply(value.atZone(zoneId))
  }

  /**
   * current datetime factory for [[EDateTime]]
   * @param zoneId [[java.time.ZoneId]]
   * @return [[EDateTime]]
   */
  def now()(implicit zoneId: ZoneId = defaultZoneId) =
    EDateTime(ZonedDateTime.now(zoneId))

  /**
   * factory for [[EDateTime]] from [[EpochMilliseconds]]
   *
   * @param milliseconds [[EpochMilliseconds]]
   * @return [[EDateTime]]
   */
  def fromEpochMilli(
    milliseconds: EpochMilliseconds
  )(implicit zoneId: ZoneId = defaultZoneId): EDateTime = {
    val dateTime = ZonedDateTimeHelper.fromEpochMillis(milliseconds)(zoneId)
    EDateTime(dateTime)
  }

  /**
   * create [[EDateTime]] from yyyy-MM-dd format String(e.g. 2017-03-15)
   *
   * @param value String formatted with 'yyyy-MM-dd'
   * @return [[EDateTime]]
   */
  def `from-yyyy-MM-dd`(value: String)(implicit zoneId: ZoneId = defaultZoneId): EDateTime = {
    require(value.length == 10, s"invalid date expression, $value length is not 10(YYYY-MM-DD)")

    val yyyy = value.substring(0, 4).toInt
    val mm = value.substring(5, 7).toInt
    val dd = value.substring(8, 10).toInt
    val date = ZonedDateTime.of(yyyy, mm, dd, 0, 0, 0, 0, zoneId)
    EDateTime(date)
  }

  /**
   * create [[EDateTime]] from yyyy-MM-dd HH:mm:ss format String(e.g. 2017-03-15 12:23:45)
   *
   * @param value String formatted with 'yyyy-MM-dd HH:mm:ss'
   * @return [[EDateTime]]
   */
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
