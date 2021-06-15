package magwer.dolphin.game.room

import magwer.dolphin.room.RoomRemodel.SlotType

class RoomModel(val matrix: Array<Array<SlotType>>) {
    val width = 27;
    val height = 15;
}