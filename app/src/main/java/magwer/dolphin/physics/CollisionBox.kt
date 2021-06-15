package magwer.dolphin.physics

interface CollisionBox {

    fun collideWith(box: CollisionBox): Boolean

}