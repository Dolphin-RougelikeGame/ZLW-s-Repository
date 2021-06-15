package magwer.dolphin.game.generator

import magwer.dolphin.api.Coord
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap

class ChapterShapeGenerator(val random: Random) {

    enum class Status {

        ROOM,
        WALL

    }

    val spaceMap = HashMap<Coord, Status>()

    fun generateMap(startX: Int, startY: Int, width: Int, height: Int): ArrayList<String> {
        val list = ArrayList<String>()
        for (y in startY until startY + height) {
            val builder = StringBuilder()
            for (x in startX until startX + width) {
                if (spaceMap[x to y] != null)
                    builder.append('*')
                else
                    builder.append(' ')
            }
            list.add(builder.toString())
        }
        return list
    }

}