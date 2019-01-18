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
            // TODO: Renable challenge 11 after fixing it.
//            ChallengeEleven,
            ChallengeTwelve,
            ChallengeThirteen,
            ChallengeFourteen)

    challenges.forEach { challenge ->
        challenge.run()
    }
}
