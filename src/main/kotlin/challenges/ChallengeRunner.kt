package challenges

import challenges.setone.*
import challenges.settwo.ChallengeEleven
import challenges.settwo.ChallengeNine
import challenges.settwo.ChallengeTen
import challenges.settwo.ChallengeTwelve

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
            ChallengeEleven,
            ChallengeTwelve)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
