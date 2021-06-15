package magwer.dolphin.physics

object CollisionHandler {

    fun collideWith(rect1: RectangleBox, rect2: RectangleBox): Boolean {
        return rect1.internal_collideWith(rect2) || rect2.internal_collideWith(rect1)
    }

    fun collideWith(circle1: CircleBox, circle2: CircleBox): Boolean {
        return circle1.internal_collideWith(circle2)
    }

    fun collideWith(rect: RectangleBox, circle: CircleBox): Boolean {
        val closestx = if (circle.x < rect.x)
            rect.x
        else if (circle.x > rect.x + rect.width)
            rect.x + rect.width
        else
            circle.x
        val closesty = if (circle.y < rect.y)
            rect.y
        else if (circle.y < rect.y + rect.height)
            rect.y + rect.height
        else
            circle.y
        val dx = closestx - circle.x
        val dy = closesty - circle.y
        val distance = Math.sqrt(dx * dx + dy * dy)
        return distance < circle.radius
    }

}