package magwer.dolphin.ui

import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.math.sqrt

class JoyStickControlTouchListener(
    private val control: FrameLayout,
    private val center: ImageView
    ) : View.OnTouchListener {

        var dragging = false
        var strengthX = 0.0
        var strengthY = 0.0
        var strength = 0.0

        private fun onDrag(event: MotionEvent) {
            val cx = control.width * 0.5
            val cy = control.height * 0.5
            val dx = event.x - cx
            val dy = event.y - cy
            var strx = dx / cx
            var stry = dy / cy
            var str = sqrt(strx * strx + stry * stry)
            if (str > 1) {
                val mul = 1.0 / str
                strx *= mul
                stry *= mul
                str *= mul
            }
            strengthX = strx
            strengthY = stry
            strength = str
        }

        private fun updateCenterLoc() {
            val cx = control.width * 0.5
            val cy = control.height * 0.5
            val x = strengthX * cx
            val y = strengthY * cy
            center.translationX = x.toFloat() + cx.toFloat() - center.width / 2.0f
            center.translationY = y.toFloat() + cy.toFloat() - center.height / 2.0f
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onDrag(event)
                    dragging = true
                    updateCenterLoc()
                }
                MotionEvent.ACTION_MOVE -> {
                    onDrag(event)
                    updateCenterLoc()
                }
                MotionEvent.ACTION_UP -> {
                    dragging = false
                    strengthX = 0.0
                    strengthY = 0.0
                    strength = 0.0
                    updateCenterLoc()
                }
            }
            return true
        }

    }