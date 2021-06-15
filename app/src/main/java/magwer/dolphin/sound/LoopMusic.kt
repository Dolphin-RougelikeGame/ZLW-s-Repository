package magwer.dolphin.sound

import java.util.*

class LoopMusic(
    private val manager: SoundManager,
    private val filePath: String,
    private val volumeMul: Double,
    private val maxloops: Int,
    private val startmills: Long,
    private val loopmills: Long,
    protected val endmills: Long,
    private val rollbackloop: Int
) : Music {

    inner class LoopTask : TimerTask() {

        var i = 1

        override fun run() {
            synchronized(this@LoopMusic) {
                if (stopping) {
                    endStage = true
                    manager.playSound(filePath + "end.ogg", volumeMul.toFloat())
                    stopTask = StopTask()
                    manager.timer.schedule(stopTask, endmills)
                    loopTask = null
                    cancel()
                    return
                }
                manager.playSound(filePath + "$i.ogg", volumeMul.toFloat())
                i++
                if (i > maxloops)
                    i = rollbackloop
            }
        }
    }

    inner class StopTask : TimerTask() {

        override fun run() {
            synchronized(manager) {
                loopTask = null
                stopTask = null
                stopping = false
                endStage = false
                manager.currentMusic = manager.nextMusic
                manager.currentMusic!!.start()
                manager.nextMusic = null
            }
        }
    }

    override var stopping = false

    var endStage = false
    var loopTask: LoopTask? = null
    var stopTask: StopTask? = null

    override fun start() {
        stopping = false
        endStage = false
        manager.playSound(filePath + "start.ogg", volumeMul.toFloat())
        loopTask = LoopTask()
        manager.timer.scheduleAtFixedRate(loopTask!!, startmills, loopmills)
    }

    override fun resume(): Boolean {
        synchronized(this) {
            if (endStage)
                return false
            stopping = false
            return true
        }
    }

    override fun stop() {
        stopping = true
    }

    override fun shutdown() {
        loopTask?.cancel()
        loopTask = null
        stopTask?.cancel()
        stopTask = null
        stopping = false
        endStage = false
    }

}