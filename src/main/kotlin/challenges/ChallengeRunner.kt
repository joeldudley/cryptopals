package challenges

import challenges.setone.*
import challenges.settwo.*

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
            ChallengeTwelve,
            ChallengeThirteen)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
