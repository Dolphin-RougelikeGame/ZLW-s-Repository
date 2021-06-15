package magwer.dolphin.game

abstract class GameObject(
    var scene: GameScene,
    var x: Double,
    var y: Double
) {

    open fun addToScene() {
        scene.internal_addGameObject(this)
    }

    open fun removeFromScene() {
        scene.internal_removeGameObject(this)
    }

    open fun changeScene(newScene: GameScene) {
        if (scene == newScene)
            return
        scene.internal_removeGameObject(this)
        scene = newScene
    }

    fun gameToScreen(coord: Double): Float {
        return (coord * 0.1).toFloat()
    }

    abstract fun onTick(deltaTime: Long)

}