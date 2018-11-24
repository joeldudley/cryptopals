package cryptopals

abstract class Challenge(val set: Int, val number: Int) {
    abstract fun passes(): Boolean

    fun run() = if (!passes()) throw Exception("Challenge $number of set $set failed.") else Unit
}
