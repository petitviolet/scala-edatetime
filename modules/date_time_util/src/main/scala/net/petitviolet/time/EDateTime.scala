package net.petitviolet.time

import java.time.format.DateTimeFormatter
import java.time._
import java.util.Locale

class EDateTime private (val value: ZonedDateTime) extends Ordered[EDateTime] {
  import EDateTime.ZonedDateTimeHelper
  import TimeOps._

  override def compare(that: EDateTime): Int =
    value.compareTo(that.value)

  lazy val epochMillis: EpochMilliseconds = value.epochMilli

  private def -(start: EDateTime): Milliseconds = {
    val end = this
    Milliseconds((end.epochMillis - start.epochMillis).value)
  }

  def +(day: Day): EDateTime = {
    EDateTime(value.plusDays(day.value.toLong))
  }

  def -(day: Day): EDateTime = {
    EDateTime(value.minusDays(day.value.toLong))
  }

  /**
   * "20161223"
   */
  lazy val `yyyy-MM-dd`: String = {
    value.format(EDateTime.`yyyy-MM-dd`)
  }

  /**
   * "2016-12-23 14:15:33"
   */
  lazy val `yyyy-MM-dd HH:mm:ss`: String = {
    value.format(EDateTime.`yyyy-MM-dd HH:mm:ss`)
  }

  override lazy val toString: String = {
    val str = this.`yyyy-MM-dd HH:mm:ss`
    s"EDateTime($str ${value.getZone.getId})"
  }

  def asDate: EDate = EDate.apply(value.toLocalDate)
}

object GlobalEDateTimeSettings {
  @volatile var zoneId: ZoneId = ZoneId.systemDefault()
  @volatile var locale: Locale = Locale.getDefault
  def zoneOffset: ZoneOffset = zoneId.getRules.getOffset(Instant.now(Clock.system(zoneId)))
}

object EDateTime {
  import GlobalEDateTimeSettings._

  def apply(value: ZonedDateTime): EDateTime =
    new EDateTime(value)

  def apply(value: LocalDateTime): EDateTime = {
    apply(value.atZone(zoneId))
  }

  def now() = EDateTime(ZonedDateTime.now(zoneId))

  /**
   * [[EpochMilliseconds]]から[[EDateTime]]を作成する
   * @param milliseconds
   * @return
   */
  def fromEpochMilli(milliseconds: EpochMilliseconds): EDateTime = {
    val dateTime = ZonedDateTimeHelper.fromEpochMillis(milliseconds)
    EDateTime(dateTime)
  }

  /**
   * yyyy-MM-dd(e.g. 2017-03-15)から[[EDateTime]]を作成する
   * @param value yyyy-MMdd形式の日付
   * @return
   */
  def `from-yyyy-MM-dd`(value: String): EDateTime = {
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
    DateTimeFormatter.ofPattern("yyyy-MM-dd", locale)

  private lazy val `yyyy-MM-dd HH:mm:ss`: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", locale)

  // 2011/01/01 00:00:00のDateTime
  lazy val `2011/01/01 00:00:00`: EDateTime =
    EDateTime(ZonedDateTime.of(2011, 1, 1, 0, 0, 0, 0, zoneId))

  private implicit class ZonedDateTimeHelper(val zonedDateTime: ZonedDateTime) extends AnyVal {
    def epochMilli: EpochMilliseconds =
      ZonedDateTimeHelper.toEpochMillis(zonedDateTime)
  }

  private object ZonedDateTimeHelper {
    // epoch milliまわりはUTCで計算する
    def fromEpochMillis(epochMilliseconds: EpochMilliseconds): ZonedDateTime =
      java.time.Instant.ofEpochMilli(epochMilliseconds.value).atZone(GlobalEDateTimeSettings.zoneId)

    def toEpochMillis(localDateTime: ZonedDateTime): EpochMilliseconds =
      EpochMilliseconds(localDateTime.toInstant.toEpochMilli)
  }
}

