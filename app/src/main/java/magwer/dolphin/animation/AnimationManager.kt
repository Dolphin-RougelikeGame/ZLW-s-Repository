package magwer.dolphin.animation

import java.util.*
import kotlin.collections.HashMap

class AnimationManager {

    private val timer = Timer()
    private val animations = HashMap<BitmapHolder, AnimationTask>()

    var paused = false

    fun startAnimation(holder: BitmapHolder, anim: Animation) {
        synchronized(this) {
            animations.remove(holder)?.cancel()
            val period = anim.duration / (anim.images.size - 1)
            val task = AnimationTask(this, holder, anim)
            timer.scheduleAtFixedRate(task, 0L, period)
        }
    }

    fun stopAnimation(holder: BitmapHolder) {
        synchronized(this) {
            animations.remove(holder)?.cancel()
        }
    }

    fun getPlaying(holder: BitmapHolder): Animation? {
        return animations[holder]?.anim
    }

    fun shutdown() {
        timer.cancel()
        animations.clear()
    }

}