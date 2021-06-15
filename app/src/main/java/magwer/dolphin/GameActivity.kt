package magwer.dolphin

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import magwer.dolphin.game.GameScene
import magwer.dolphin.game.MainCharacter
import magwer.dolphin.game.generator.ChapterShapeGenerator
import magwer.dolphin.game.generator.RoomShapeGenerator
import magwer.dolphin.ui.JoyStickControlTouchListener
import java.util.*
import kotlin.concurrent.timerTask

class GameActivity : Activity() {

    private val gameScene by lazy { GameScene(findViewById(R.id.openGLView)) }
    private val timer by lazy { Timer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val leftcontrol = findViewById<FrameLayout>(R.id.left_control)
        val leftcontrolcenter = findViewById<ImageView>(R.id.left_control_center)
        val leftcontroller = JoyStickControlTouchListener(leftcontrol, leftcontrolcenter)
        leftcontrol.setOnTouchListener(leftcontroller)

        timer.schedule(timerTask {
            MainCharacter(gameScene, leftcontroller).addToScene()
            gameScene.startTicking()
        }, 1000L)

        //val c = ChapterShapeGenerator(Random())
        //val a = RoomShapeGenerator(c, 50 to 50, 30, 0.6, 1.0, 1.0)
//
        //while (true) {
        //    val success = a.trySpread()
        //    if (!success)
        //        return
        //    val list = c.generateMap(0, 0, 100, 100)
        //    for (line in list)
        //        println(line)
        //    println()
        //    println()
        //}

    }

    override fun onDestroy() {
        super.onDestroy()

        gameScene.shutdown()
    }

}