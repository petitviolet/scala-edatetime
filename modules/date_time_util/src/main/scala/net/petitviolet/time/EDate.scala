package net.petitviolet.time

import java.time._
import java.time.format.DateTimeFormatter

import scala.util.{ Failure, Try }

class EDate private (val value: LocalDate) extends Ordered[EDate] { self =>
  import EDate.LocalDateHelper

  override def compare(that: EDate): Int = self.value.compareTo(that.value)

  def +(day: Day): EDate = new EDate(value.plusDays(day.value))

  def -(day: Day): EDate = new EDate(value.minusDays(day.value))

  lazy val epochMillis: EpochMilliseconds = value.epochMills

  /**
   * "2016-12-23"
   */
  lazy val `yyyy-MM-dd`: String = {
    value.format(EDate.`yyyy-MM-dd`)
  }

  /**
   * "20161223"
   */
  lazy val `yyyyMMdd`: String = {
    value.format(EDate.`yyyyMMdd`)
  }
}

object EDate {

  import GlobalEDateTimeSettings._

  def fromEpochMillis(millis: EpochMilliseconds): EDate = EDateTime.fromEpochMilli(millis).asDate

  def fromString(str: String): Try[EDate] =
    Try {
      val date = str.length match {
        case 8 => LocalDate.parse(str, `yyyyMMdd`)
        case 10 if str.contains("-") => LocalDate.parse(str, `yyyy-MM-dd`)
        case 10 if str.contains("_") => LocalDate.parse(str, `yyyy_MM_dd`)
      }
      apply(date)
    }.recoverWith {
      case t: Throwable =>
        Failure(new Exception(s"String($str}) invalid date format. msg = ${ t.getMessage }"))
    }

  def apply(localDate: LocalDate): EDate = new EDate(localDate)

  def apply(localDateTime: LocalDateTime): EDate = new EDate(localDateTime.toLocalDate)

  def now(): EDate = new EDate(LocalDate.now(zoneId))

  private def pattern(pt: String) =
    DateTimeFormatter.ofPattern(pt, locale)

  private lazy val `yyyy_MM_dd`: DateTimeFormatter = pattern("yyyy_MM_dd")

  private lazy val `yyyy-MM-dd`: DateTimeFormatter = pattern("yyyy-MM-dd")

  private val `yyyyMMdd`: DateTimeFormatter = pattern("yyyyMMdd")

  private implicit class LocalDateHelper(val date: LocalDate) extends AnyVal {
    def epochMills: EpochMilliseconds = {
      val milli = date.atTime(0, 0).toInstant(zoneOffset).toEpochMilli
      EpochMilliseconds(milli)
    }
  }
}

