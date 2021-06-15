package magwer.dolphin.game.generator

import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap

typealias Coord = Pair<Int, Int>

val Coord.adjacents
    get() = arrayOf(
        Coord(first - 1, second),
        Coord(first + 1, second),
        Coord(first, second - 1),
        Coord(first, second + 1)
    )

val Coord.nearby
    get() = arrayOf(
        Coord(first - 1, second),
        Coord(first + 1, second),
        Coord(first, second - 1),
        Coord(first, second + 1),

        Coord(first - 1, second - 1),
        Coord(first + 1, second - 1),
        Coord(first + 1, second + 1),
        Coord(first - 1, second + 1)
    )

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