package magwer.dolphin.game

import magwer.dolphin.api.RenderedObject
import magwer.dolphin.api.loadBitmapAsset
import magwer.dolphin.graphics.GLSquare
import magwer.dolphin.physics.Collider
import magwer.dolphin.physics.RectangleBox
import magwer.dolphin.ui.JoyStickControlTouchListener

class MainCharacter(scene: GameScene, private val controller: JoyStickControlTouchListener) : GameObject(scene),
    RenderedObject {

    var x = 0.0
    var y = 0.0
    val width = 1.0
    val height = 1.0

    override val glShape = GLSquare(
        scene.view.shaderProgram,
        loadBitmapAsset(scene.context, "texture/main_character.png"),
        0.0f,
        0.0f,
        gameToScreen(width),
        gameToScreen(height)
    )

    val collider = Collider(this, scene, RectangleBox(0.0, 0.0, width, height), 0)

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
            val newbox = collider.box.copyTo(x, y)
            collider.box = newbox
        }
        glShape.screenX = gameToScreen(x)
        glShape.screenY = gameToScreen(y)
        glShape.updateCoords()
    }

    override fun onTick(deltaTime: Long) {
        val mx = controller.strengthX * deltaTime * 0.004
        val my = - controller.strengthY * deltaTime * 0.004
        move(mx, my)
    }

}