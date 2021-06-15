package magwer.dolphin.game

import magwer.dolphin.api.RenderedObject
import magwer.dolphin.api.RenderedScene
import magwer.dolphin.graphics.OpenGLView
import magwer.dolphin.physics.Collider
import magwer.dolphin.physics.CollisionRule
import magwer.dolphin.physics.CollisionScene
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask

class GameScene(override val view: OpenGLView) : RenderedScene, CollisionScene {

    override val collisionObjects = HashMap<Int, ArrayList<Collider<*>>>()
    override val collisionRule = CollisionRule()
    private val gameObjects = ArrayList<GameObject>()

    val context
        get() = view.context

    private fun onTick(deltaTime: Long) {
        val currentobjects = synchronized(gameObjects) {
            val list = ArrayList<GameObject>()
            list.addAll(gameObjects)
            list
        }
        for (obj in currentobjects)
            obj.onTick(deltaTime)
    }

    fun internal_addGameObject(obj: GameObject) {
        synchronized(gameObjects) {
            if (!gameObjects.contains(obj))
                gameObjects.add(obj)
            if (obj is RenderedObject)
                view.renderer.addShape(obj.glShape)
        }
    }

    fun internal_removeGameObject(obj: GameObject) {
        synchronized(gameObjects) {
            gameObjects.remove(obj)
            if (obj is RenderedObject)
                view.renderer.removeShape(obj.glShape)
        }
    }

    fun startTicking(timer: Timer) {
        var lastTick = System.currentTimeMillis()
        timer.scheduleAtFixedRate(timerTask {
            val old = lastTick
            lastTick = System.currentTimeMillis()
            onTick(lastTick - old)
        }, 0L, 50L)
    }

}