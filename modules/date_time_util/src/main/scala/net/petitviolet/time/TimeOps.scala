package net.petitviolet.time

import scala.annotation.implicitNotFound

object TimeOps {
  implicit class TimeOperatorOps[T](val target: T) extends AnyVal {
    def +(other: T)(implicit ops: TimeOps[T]): T =
      ops.add(target, other)
    def -(other: T)(implicit ops: TimeOps[T]): T =
      ops.minus(target, other)
  }

  implicit val epochMillOps: TimeOps[EpochMilliseconds] =
    new TimeOps[EpochMilliseconds] {
      override def add(first: EpochMilliseconds, second: EpochMilliseconds): EpochMilliseconds =
        EpochMilliseconds(first.value + second.value)
      override def minus(first: EpochMilliseconds, second: EpochMilliseconds): EpochMilliseconds =
        EpochMilliseconds(first.value - second.value)
    }

  implicit val millOps: TimeOps[Milliseconds] = new TimeOps[Milliseconds] {
    override def add(first: Milliseconds, second: Milliseconds): Milliseconds =
      Milliseconds(first.value + second.value)
    override def minus(first: Milliseconds, second: Milliseconds): Milliseconds =
      Milliseconds(first.value - second.value)
  }

  implicit val hourOps: TimeOps[Hour] = new TimeOps[Hour] {
    override def add(first: Hour, second: Hour): Hour =
      Hour(first.value + second.value)
    override def minus(first: Hour, second: Hour): Hour =
      Hour(first.value - second.value)
  }

  implicit val dayOps: TimeOps[Day] = new TimeOps[Day] {
    override def add(first: Day, second: Day): Day =
      Day(first.value + second.value)
    override def minus(first: Day, second: Day): Day =
      Day(first.value - second.value)
  }
}

@implicitNotFound("you should implement TimeOps[T] or `import net.petitviolet.time._`")
sealed trait TimeOps[T] extends Any {
  def add(first: T, second: T): T
  def minus(first: T, second: T): T
}

// elapsed milliseconds from 1970/01/01
case class EpochMilliseconds(value: Long) extends AnyVal with Ordered[EpochMilliseconds] {
  override def compare(that: EpochMilliseconds): Int =
    this.value compare that.value

  def +(milliseconds: Milliseconds): EpochMilliseconds =
    copy(value + milliseconds.value)
}

case class Milliseconds(value: Long) extends AnyVal with Ordered[Milliseconds] {
  def asHour: Hour = Hour((value / (1000L * 60L * 60L)).toInt)
  def isValid: Boolean = value >= 0

  override def compare(that: Milliseconds): Int = this.value compare that.value
}

case class Hour(value: Int) extends AnyVal with Ordered[Hour] {
  def asMillis: Milliseconds = Milliseconds(value * 60L * 60L * 1000L)
  def asDay: Day = Day(value / 24)
  def isValid: Boolean = value >= 0

  override def compare(that: Hour): Int = this.value compare that.value
}

case class Day(value: Int) extends AnyVal with Ordered[Day] {
  def asHour: Hour = Hour(value * 24)
  def isValid: Boolean = value >= 0

  override def compare(that: Day): Int = this.value compare that.value
}

