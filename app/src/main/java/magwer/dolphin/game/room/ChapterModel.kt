package magwer.dolphin.game.room

class ChapterModel {

    class RoomLoc(room: RoomModel, x: Int, y: Int)

    val rooms = HashMap<RoomModel, ArrayList<RoomLoc>>()
    val portals = HashMap<RoomLoc, RoomLoc>()

}