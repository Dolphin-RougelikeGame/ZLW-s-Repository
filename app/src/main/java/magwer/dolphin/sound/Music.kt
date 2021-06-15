package magwer.dolphin.sound

interface Music {

    var stopping: Boolean

    fun start()
    fun resume(): Boolean
    fun stop()
    fun shutdown()

}