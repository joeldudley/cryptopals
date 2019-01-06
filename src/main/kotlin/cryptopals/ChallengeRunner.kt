package cryptopals

import cryptopals.setone.*
import cryptopals.settwo.ChallengeNine

fun main(args: Array<String>) {
    val challenges = listOf(
            ChallengeOne,
            ChallengeTwo,
            ChallengeThree,
            ChallengeFour,
            ChallengeFive,
            ChallengeSix,
            ChallengeSeven,
            ChallengeEight,
            ChallengeNine)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
