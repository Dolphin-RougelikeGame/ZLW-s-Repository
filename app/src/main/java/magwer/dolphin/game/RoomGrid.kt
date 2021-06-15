package magwer.dolphin.game

import magwer.dolphin.api.Coord
import magwer.dolphin.physics.Collider


class RoomGrid(
    val startX: Int,
    val startY: Int,
    val width: Int,
    val height: Int
) {

    private val objectMap = HashMap<GameObject, Coord>()
    private val slotMap = HashMap<Coord, ArrayList<GameObject>>()

    fun updateSlot(obj: GameObject) {
        val x = (obj.x - startX).toInt()
        val y = (obj.y - startY).toInt()
        val coord = x to y
        val old = objectMap.put(obj, coord)
        slotMap[old]?.remove(obj)
        slotMap.getOrPut(coord, { ArrayList() }).add(obj)
        objectMap[obj] = coord
    }

    fun findPath(collider: Collider<*>, from: Coord, to: Coord) {

    }

}