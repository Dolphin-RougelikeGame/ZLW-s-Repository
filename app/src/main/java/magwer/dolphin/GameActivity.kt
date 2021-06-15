package magwer.dolphin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import magwer.dolphin.api.loadBitmapAsset
import magwer.dolphin.game.GameScene
import magwer.dolphin.game.MainCharacter
import magwer.dolphin.graphics.GLSquare
import magwer.dolphin.ui.JoyStickControlTouchListener
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.sqrt

class GameActivity : Activity() {

    private val timer by lazy { Timer() }
    private var gameScene: GameScene? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameScene = GameScene(findViewById(R.id.openGLView))

        gameScene!!.startTicking(timer)

        val leftcontrol = findViewById<FrameLayout>(R.id.left_control)
        val leftcontrolcenter = findViewById<ImageView>(R.id.left_control_center)
        val leftcontroller = JoyStickControlTouchListener(leftcontrol, leftcontrolcenter)
        leftcontrol.setOnTouchListener(leftcontroller)

        timer.schedule(timerTask {
            MainCharacter(gameScene!!, leftcontroller).addToScene()
        }, 1000L)

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}