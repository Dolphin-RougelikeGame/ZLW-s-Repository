package magwer.dolphin.game.`object`

import magwer.dolphin.api.Coord
import magwer.dolphin.game.GameScene
import magwer.dolphin.game.room.RoomGrid
import magwer.dolphin.physics.Collider
import magwer.dolphin.physics.CollisionRule
import magwer.dolphin.physics.RectangleBox

class RoomStaticObject(
    scene: GameScene,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) : GameObject(scene) {

    val collider = Collider(
        this,
        scene,
        RectangleBox(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()),
        CollisionRule.STATIC_CHANNEL
    )

    override fun addToScene() {
        super.addToScene()
        collider.addToScene()
    }

    override fun removeFromScene() {
        super.removeFromScene()
        collider.removeFromScene()
    }

    override fun onTick(deltaTime: Long) {

    }

    override fun getRoomCoords(roomGrid: RoomGrid): ArrayList<Coord> {
        val list = ArrayList<Coord>()
        for (x in x until x + width)
            for (y in y until y + height)
                list.add(roomGrid.gameToRoom(x.toDouble(), y.toDouble()))
        return list
    }
}