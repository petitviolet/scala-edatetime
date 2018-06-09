package net.petitviolet.time.cache

import java.util.concurrent.atomic.AtomicReference
import java.util.{ Timer, TimerTask }

import net.petitviolet.time.EDateTime

object CachedDateTime {
  def init(): Unit = {
    CachedDateTimeUpdater.init()
  }
  def shutdown(): Unit = CachedDateTimeUpdater.shutdown()

  def now(): EDateTime = CachedDateTimeHolder.get()
}

private object CachedDateTimeUpdater {
  private val updateTask: TimerTask = new TimerTask {
    override def run(): Unit = CachedDateTimeHolder.update()
  }
  private lazy val timer = new Timer()

  def init(): Unit = {
    CachedDateTimeHolder.update()
    timer.schedule(updateTask, 0L, 1000L)
    sys.addShutdownHook {
      timer.cancel()
    }
  }

  def shutdown(): Unit = {
    timer.cancel()
  }
}

private object CachedDateTimeHolder {
  private val _dateTime = {
    new AtomicReference[EDateTime]
  }

  def get(): EDateTime = {
    _dateTime.get()
  }

  def update(): Unit = {
    val _ = _dateTime.getAndSet(EDateTime.now())
  }

}
