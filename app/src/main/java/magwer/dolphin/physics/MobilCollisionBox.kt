package magwer.dolphin.physics

interface MobilCollisionBox : CollisionBox {

    val x: Double
    val y: Double

    fun copyTo(toX: Double, toY: Double): MobilCollisionBox

}