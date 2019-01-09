package challenges

abstract class Challenge(private val set: Int, private val number: Int) {
    abstract fun passes(): Boolean

    fun run() = if (!passes()) throw Exception("Challenge $number of set $set failed.") else Unit
}
