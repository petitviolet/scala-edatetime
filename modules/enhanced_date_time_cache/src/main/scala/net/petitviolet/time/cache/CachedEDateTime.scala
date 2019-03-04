package net.petitviolet.time.cache

import java.util.concurrent.atomic.AtomicReference
import java.util.{ Timer, TimerTask }

import net.petitviolet.time.EDateTime

import scala.concurrent.duration._

object CachedEDateTime {
  @volatile private var updater: CachedEDateTimeUpdater = _

  private lazy val init: Unit = {
    initialize()
    ()
  }

  def initialize(): Unit = {
    updater = new CachedEDateTimeUpdater()
    updater.init()
  }

  def shutdown(): Unit = {
    updater.shutdown()
    updater = null
  }

  def now(): EDateTime = {
    init // call once only
    CachedEDateTimeHolder.get()
  }
}

private class CachedEDateTimeUpdater() {
  private val updateTask: TimerTask = new TimerTask {
    override def run(): Unit = CachedEDateTimeHolder.update()
  }
  private lazy val timer = new Timer()

  def init(period: Duration = 800L.milliseconds): Unit = {
    updateTask.run() // 即時実行
    timer.schedule(updateTask, 0L, period.toMillis)
    sys.addShutdownHook {
      shutdown()
    }
  }

  def shutdown(): Unit = {
    timer.cancel()
    updateTask.cancel()
  }
}

private object CachedEDateTimeHolder {
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
