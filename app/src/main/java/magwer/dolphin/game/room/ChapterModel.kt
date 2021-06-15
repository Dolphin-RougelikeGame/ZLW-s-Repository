package magwer.dolphin.game.room

class ChapterModel {

    class RoomLoc(room: RoomModel, x: Int, y: Int){}

    private var cnt = 0
    private val rooms = HashMap<Int, RoomModel>()

    val portalsOfRooms = HashMap<RoomModel, ArrayList<RoomLoc>>()
    val portals = HashMap<RoomLoc, RoomLoc>()

    public fun getRoom(room: RoomModel){
        rooms.put(cnt, room)
        cnt++
    }
}