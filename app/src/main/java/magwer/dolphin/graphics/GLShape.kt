package magwer.dolphin.graphics

interface GLShape {

    fun onRatioChange(ratio: Float)
    fun draw(viewPort: OpenGLViewport)

}