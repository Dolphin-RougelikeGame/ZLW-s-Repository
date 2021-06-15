package magwer.dolphin.game.room

class RoomModel(val width: Int, val height: Int) {

    enum class SlotType {

        WALL,
        INSIDE,
        PORTAL,
        NONE

    }

    val matrix = Array(width) { Array(height) { SlotType.NONE} }

}