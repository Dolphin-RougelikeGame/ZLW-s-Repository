package magwer.dolphin.physics

class CollisionRule {

    enum class Rule {

        BLOCK,
        OVERLAP,
        IGNORE

    }

    private val map = HashMap<Pair<Int, Int>, Rule>()

    fun setRule(channel1: Int, channel2: Int, rule: Rule) {
        if (channel1 > channel2)
            return setRule(channel2, channel1, rule)
        if (rule == Rule.IGNORE)
            map.remove(channel1 to channel2)
        else
            map[channel1 to channel2] = rule
    }

    fun blocksWith(channel1: Int, channel2: Int): Boolean {
        if (channel1 > channel2)
            return blocksWith(channel2, channel1)
        return map[channel1 to channel2] == Rule.BLOCK
    }

    fun overlapWith(channel1: Int, channel2: Int): Boolean {
        if (channel1 > channel2)
            return overlapWith(channel2, channel1)
        return map[channel1 to channel2] == Rule.OVERLAP
    }

}