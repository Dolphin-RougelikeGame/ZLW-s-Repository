package magwer.dolphin.physics

import magwer.dolphin.game.GameObject

class Collider<T: CollisionBox>(val owner: GameObject, val scene: CollisionScene, var box: T, val channel: Int) {

    init {
        scene.internal_addCollisionObject(this)
    }

    fun checkCollision(box: CollisionBox = this.box): Pair<ArrayList<Collider<*>>, ArrayList<Collider<*>>> {
        synchronized(box) {
            return scene.internal_checkCollision(this, box)
        }
    }

}