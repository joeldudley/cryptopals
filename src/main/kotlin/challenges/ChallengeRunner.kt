package challenges

import challenges.setone.*
import challenges.settwo.ChallengeEleven
import challenges.settwo.ChallengeNine
import challenges.settwo.ChallengeTen

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
