package challenges.settwo

import challenges.Challenge
import ciphers.AesEcbCipher
import ciphers.Cipher
import utilities.determineBlockSize
import utilities.toAscii
import kotlin.random.Random

/*
Write a k=v parsing routine, as if for a structured cookie. The routine should take:

    foo=bar&baz=qux&zap=zazzle

... and produce:

    {
      foo: 'bar',
      baz: 'qux',
      zap: 'zazzle'
    }

(you know, the object; I don't care if you convert it to JSON).

Now write a function that encodes a user profile in that format, given an email address. You should have something
like:

    profile_for("foo@bar.com")

... and it should produce:

    {
      email: 'foo@bar.com',
      uid: 10,
      role: 'user'
    }

... encoded as:

    email=foo@bar.com&uid=10&role=user

Your "profile_for" function should not allow encoding metacharacters (& and =). Eat them, quote them, whatever you want
to do, but don't let people set their email address to "foo@bar.com&role=admin".

Now, two more easy functions. Generate a random AES key, then:

A. Encrypt the encoded user profile under the key; "provide" that to the "attacker".
B. Decrypt the encoded user profile and parse it.

Using only the user input to profile_for() (as an oracle to generate "valid" ciphertexts) and the ciphertexts
themselves, make a role=admin profile.
*/
object ChallengeThirteen : Challenge(2, 13) {
    override fun passes(): Boolean {
        val cipher = AesEcbCipher()
        cipher.key = Random.nextBytes(16)

        val encryptedAdminProfile = generateAdminProfile(cipher)
        val adminProfile = decryptProfile(cipher, encryptedAdminProfile)

        return adminProfile["role"] == "admin"
    }
}

fun generateAndEncryptProfile(cipher: Cipher, email: String): ByteArray {
    val emailWithoutMetachars = email.replace("&", "").replace("=", "")
    val profile = "email=$emailWithoutMetachars&uid=10&role=user"

    return cipher.encrypt(profile.toByteArray())
}

fun decryptProfile(cipher: Cipher, encryptedProfile: ByteArray): Map<String, String> {
    val profileBytes = cipher.decrypt(encryptedProfile)
    val profileString = profileBytes.toAscii()
    return parseKeyValuesIntoMap(profileString)
}

fun parseKeyValuesIntoMap(keyValues: String): Map<String, String> {
    val keyValuePairs = keyValues.split('&')

    return keyValuePairs.map { keyValuePair ->
        val (key, value) = keyValuePair.split('=')
        key to value
    }.toMap()
}

fun generateAdminProfile(cipher: Cipher): ByteArray {
    val blocksize = determineBlockSize(cipher)

    // We need a username of a length that will place the role at the start of the final ciphertext block.
    var requiredUsernameLength = 0 - "email=".length - "&uid=99&role=".length
    while (requiredUsernameLength < 1) requiredUsernameLength += blocksize
    val usernameOfRequiredLength = "a".repeat(requiredUsernameLength)

    val userProfileCipher = generateAndEncryptProfile(cipher, usernameOfRequiredLength)
    val userProfileWithoutRoleBlock = userProfileCipher.sliceArray(0 until userProfileCipher.size - blocksize)

    val adminRoleCipherBlock = cipher.encrypt("admin".toByteArray())
    return userProfileWithoutRoleBlock + adminRoleCipherBlock
}
