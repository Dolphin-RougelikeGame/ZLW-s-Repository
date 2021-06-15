package magwer.dolphin.game

import magwer.dolphin.graphics.OpenGLView
import magwer.dolphin.sound.SoundManager
import java.util.*

class Game(val view: OpenGLView) {

    private val scenes = ArrayList<GameScene>()
    val soundManager = SoundManager(this)
    val sceneTimer = Timer()

    val assets
        get() = view.context.assets
    val context
        get() = view.context

    fun internal_addScene(scene: GameScene) {
        if (!scenes.contains(scene))
            scenes.add(scene)
    }

    fun shutdown() {
        for (scene in scenes)
            scene.shutdown()
    }

}