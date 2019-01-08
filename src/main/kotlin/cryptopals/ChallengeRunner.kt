package cryptopals

import cryptopals.setone.*
import cryptopals.settwo.ChallengeEleven
import cryptopals.settwo.ChallengeNine
import cryptopals.settwo.ChallengeTen

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
            ChallengeNine,
            ChallengeTen,
            ChallengeEleven)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
