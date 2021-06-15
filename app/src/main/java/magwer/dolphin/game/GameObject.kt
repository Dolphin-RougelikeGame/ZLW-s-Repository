package magwer.dolphin.game

abstract class GameObject(val scene: GameScene) {

    open fun addToScene() {
        scene.internal_addGameObject(this)
    }

    open fun removeFromScene() {
        scene.internal_removeGameObject(this)
    }

    fun gameToScreen(coord: Double): Float {
        return (coord * 0.1).toFloat()
    }

    abstract fun onTick(deltaTime: Long)

}