package cryptopals

import cryptopals.setone.*

fun main(args: Array<String>) {
    val challenges = listOf(
            ChallengeOne,
            ChallengeTwo,
            ChallengeThree,
            ChallengeFour,
            ChallengeFive,
            ChallengeSix,
            ChallengeSeven)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
