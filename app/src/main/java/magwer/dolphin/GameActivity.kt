package magwer.dolphin

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import magwer.dolphin.game.Game
import magwer.dolphin.game.GameScene
import magwer.dolphin.game.`object`.MainCharacter
import magwer.dolphin.game.TempBoxCharacter
import magwer.dolphin.sound.LoopMusic
import magwer.dolphin.ui.JoyStickControlTouchListener
import java.util.*
import kotlin.concurrent.timerTask

class GameActivity : Activity() {

    private val game by lazy { Game(findViewById(R.id.openGLView)) }
    private val timer by lazy { Timer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val leftcontrol = findViewById<FrameLayout>(R.id.left_control)
        val leftcontrolcenter = findViewById<ImageView>(R.id.left_control_center)
        val leftcontroller = JoyStickControlTouchListener(leftcontrol, leftcontrolcenter)
        leftcontrol.setOnTouchListener(leftcontroller)

        val initialScene = GameScene(game)

        timer.schedule(timerTask {
            MainCharacter(
                initialScene,
                leftcontroller
            ).addToScene()
            TempBoxCharacter(initialScene, 0, 0).addToScene()
            TempBoxCharacter(initialScene, 0, 1).addToScene()
            TempBoxCharacter(initialScene, 1, 1).addToScene()
            TempBoxCharacter(initialScene, 1, 2).addToScene()
            initialScene.startTicking()
            game.view.renderer.viewPort.scale = 0.5f
        }, 1000L)

        val m =
            LoopMusic(
                game.soundManager,
                "airbattlecombat/",
                1.0,
                9,
                10092,
                4536,
                2338,
                6
            )


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

        game.shutdown()
    }

}