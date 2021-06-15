package magwer.dolphin.api

import magwer.dolphin.physics.Collider
import magwer.dolphin.physics.CollisionBox
import magwer.dolphin.physics.CollisionRule

interface CollisionScene {

    val collisionRule: CollisionRule
    val collisionObjects: HashMap<Int, ArrayList<Collider<*>>>

    fun internal_addCollisionObject(collider: Collider<*>) {
        collisionObjects.getOrPut(collider.channel, { ArrayList() }).add(collider)
    }

    fun internal_checkCollision(collider: Collider<*>, box: CollisionBox): Pair<ArrayList<Collider<*>>, ArrayList<Collider<*>>> {
        val blocks = ArrayList<Collider<*>>()
        val overlaps = ArrayList<Collider<*>>()
        for ((channel, colliders) in collisionObjects)
            if (collisionRule.blocksWith(channel, collider.channel)) {
                for (c in colliders)
                    if (c != collider && c.box.collideWith(box))
                        blocks.add(c)
            }
            else if (collisionRule.overlapWith(channel, collider.channel)) {
                for (c in colliders)
                    if (c != collider && c.box.collideWith(box))
                        overlaps.add(c)
            }
        return blocks to overlaps
    }

}