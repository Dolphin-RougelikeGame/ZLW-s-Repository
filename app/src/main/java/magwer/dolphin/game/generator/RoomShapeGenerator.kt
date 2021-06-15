package magwer.dolphin.game.generator

import magwer.dolphin.api.clamp
import kotlin.math.min

class RoomShapeGenerator(
    private val chapter: ChapterShapeGenerator,
    private val startLoc: Coord,
    private val targetSize: Int,
    private val sizeFactor: Double,
    private val shapeFactor: Double,
    private val rejectFactor: Double
) {

    private val inners = ArrayList<Coord>()
    private val edges = ArrayList<Coord>()
    private val nexts = ArrayList<Coord>()
    private var size = 1

    init {
        inners.add(startLoc)
        edges.addAll(startLoc.adjacents)
        nexts.addAll(startLoc.adjacents)
    }

    fun spread() {
        val next = nexts.removeAt(0)
        if (inners.contains(next))
            return

        var nearbys = 0
        var walls = 0
        for (coord in next.nearby) {
            val status = chapter.spaceMap[coord]
            if (status == ChapterShapeGenerator.Status.ROOM)
                nearbys++
            else if (status == ChapterShapeGenerator.Status.WALL)
                walls++
        }

        val chance = clamp(
            if (shapeFactor > 0)
                min(nearbys + 1.0, 8.0) / 8.0 * shapeFactor
            else
                -min(9.0 - nearbys, 8.0) / 8.0 * shapeFactor, 0.0, 1.0
        ) * clamp(
            (8 - walls * rejectFactor),
            0.0,
            1.0
        ) * sizeFactor

        if (chapter.random.nextDouble() <= chance) {
            inners.add(next)
            edges.remove(next)
            for (coord in next.adjacents) {
                if (!inners.contains(coord)) {
                    edges.add(coord)
                    nexts.add(coord)
                }
            }
            chapter.spaceMap[next] = ChapterShapeGenerator.Status.ROOM
        }

    }

    fun trySpread(): Boolean {
        if (nexts.isEmpty())
            return false
        val sizediff = targetSize - inners.size
        val chance = if (sizediff < 0)
            Math.pow(targetSize.toDouble() / (sizediff + targetSize), 3.0)
        else
            1.0
        if (chapter.random.nextDouble() <= chance) {
            spread()
            return true
        }
        return false
    }

}