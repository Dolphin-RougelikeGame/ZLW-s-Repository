package magwer.dolphin.game

import magwer.dolphin.animation.Animation
import magwer.dolphin.animation.BitmapHolder
import magwer.dolphin.api.RenderedObject
import magwer.dolphin.api.loadBitmapAsset
import magwer.dolphin.graphics.GLSquare
import magwer.dolphin.physics.Collider
import magwer.dolphin.physics.RectangleBox
import magwer.dolphin.ui.JoyStickControlTouchListener

class MainCharacter(scene: GameScene, private val controller: JoyStickControlTouchListener) :
    GameObject(scene, 0.0, 0.0),
    RenderedObject {

    val width = 1.0
    val height = 1.0

    override val glShape = GLSquare(
        scene.view.shaderProgram,
        BitmapHolder(loadBitmapAsset(scene.context, "texture/main_character.png")),
        gameToScreen(x - width * 0.5),
        gameToScreen(y - height * 0.5),
        gameToScreen(width),
        gameToScreen(height)
    )

    val collider = Collider(this, scene, RectangleBox(x - width * 0.5, y - height * 0.5, width, height), 0)

    fun move(dx: Double, dy: Double) {
        if (dx == 0.0 && dy == 0.0)
            return
        this.x += dx
        this.y += dy
        updateLoc()
    }

    fun moveTo(toX: Double, toY: Double) {
        if (toX == x && toY == y)
            return
        this.x = toX
        this.y = toY
        updateLoc()
    }

    fun updateLoc() {
        synchronized(collider.box) {
            val newbox = collider.box.copyTo(x - width * 0.5, y - height * 0.5)
            collider.box = newbox
        }
        glShape.screenX = gameToScreen(x - width * 0.5)
        glShape.screenY = gameToScreen(y - height * 0.5)
        glShape.updateCoords()
        scene.game.view.renderer.viewPort.let {
            it.transX = gameToScreen(x)
            it.transY = gameToScreen(y)
        }
    }

    override fun addToScene() {
        super.addToScene()
        scene.animationManager.startAnimation(
            glShape.texture,
            Animation(
                800,
                true,
                loadBitmapAsset(scene.context, "texture/main_character.png"),
                loadBitmapAsset(scene.context, "texture/main_character1.png")
            )
        )
    }

    override fun removeFromScene() {
        super.removeFromScene()
        scene.animationManager.stopAnimation(glShape.texture)
    }

    override fun onTick(deltaTime: Long) {
        val mx = controller.strengthX * deltaTime * 0.004
        val my = -controller.strengthY * deltaTime * 0.004
        move(mx, my)
    }

}