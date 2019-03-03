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
      override def add(first: EpochMilliseconds,
                       second: EpochMilliseconds): EpochMilliseconds =
        EpochMilliseconds(first.value + second.value)
      override def minus(first: EpochMilliseconds,
                         second: EpochMilliseconds): EpochMilliseconds =
        EpochMilliseconds(first.value - second.value)
    }
}

@implicitNotFound(
  "you should implement TimeOps[T] or `import net.petitviolet.time._`")
sealed trait TimeOps[T] extends Any {
  def add(first: T, second: T): T
  def minus(first: T, second: T): T
}

// elapsed milliseconds from 1970/01/01
case class EpochMilliseconds(value: Long)
    extends AnyVal
    with Ordered[EpochMilliseconds] {
  override def compare(that: EpochMilliseconds): Int =
    this.value compare that.value
}

