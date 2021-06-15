package magwer.dolphin.animation

import java.util.*

class AnimationTask(private val manager: AnimationManager, private val holder: BitmapHolder, val anim: Animation) : TimerTask() {

    var i = 0

    override fun run() {
        synchronized(manager) {
            if (manager.paused)
                return
            holder.bitmap = anim.images[i++]
            if (i == anim.images.size)
                if (anim.repeat)
                    i = 0
                else
                    manager.stopAnimation(holder)
        }
    }

}