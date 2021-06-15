package magwer.dolphin.sound

import android.media.MediaPlayer
import java.util.*

class SimpleMusic(private val manager: SoundManager, fileName: String, val volumeMul: Float) :
    Music {

    inner class StopTask : TimerTask() {

        private var volume = volumeMul
        private val fade = volume * FADE_SPEED

        override fun run() {
            if (stopping) {
                volume -= fade
                if (volume <= 0) {
                    player.stop()
                    synchronized(manager) {
                        stopTask = null
                        stopping = false
                        manager.currentMusic = manager.nextMusic
                        manager.currentMusic!!.start()
                        manager.nextMusic = null
                    }
                    cancel()
                    return
                }
                player.setVolume(volume, volumeMul)
            }
            else {
                volume += fade
                if (volume >= volumeMul) {
                    player.setVolume(volumeMul, volumeMul)
                    synchronized(manager) {
                        stopTask = null
                    }
                    cancel()
                    return
                }
                player.setVolume(volume, volume)
            }
        }
    }

    override var stopping = false

    private val player = MediaPlayer()
    private var stopTask: StopTask? = null

    init {
        player.setDataSource(manager.game.assets.openFd(fileName))
        player.prepare()
    }

    override fun start() {
        stopping = false
        player.setVolume(volumeMul, volumeMul)
        player.seekTo(0)
        player.start()
    }

    override fun resume(): Boolean {
        stopping = false
        return true
    }

    override fun stop() {
        stopping = true
        if (stopTask != null)
            return
        stopTask = StopTask()
        manager.timer.scheduleAtFixedRate(stopTask, 0L, FADE_INTERVAL)
    }

    override fun shutdown() {
        stopTask?.cancel()
        stopTask = null
        stopping = false
    }

    companion object {

        const val FADE_INTERVAL = 50L
        const val FADE_SPEED = 0.08f

    }

}