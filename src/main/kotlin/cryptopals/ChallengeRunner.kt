package cryptopals

import cryptopals.setone.*

fun main(args: Array<String>) {
    val challenges = listOf(
            ChallengeOne,
            ChallengeTwo,
            ChallengeThree,
            ChallengeFour)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
