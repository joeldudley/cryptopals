package cryptopals.setone

import cryptopals.Challenge
import utilities.*
import java.io.File
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec
import sun.security.krb5.Confounder.bytes
import java.io.ByteArrayInputStream
import javax.crypto.CipherInputStream


object ChallengeSeven : Challenge(1, 7) {
    override fun passes(): Boolean {
        val providedEncryptedBase64String = File("src/main/resources/challengedata/7.txt").readText().filter { it != '\n' }
        val providedKey = "YELLOW SUBMARINE"

        val encryptedBytes = providedEncryptedBase64String.base64ToBytes()
        val keyBytes = providedKey.toByteArray()

        val secretKeySpec = SecretKeySpec(keyBytes, "AES")
        val cipher = Cipher.getInstance("AES/ECB/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)

        decryptedBytes

        return true
    }
}

fun main(args: Array<String>) {
    ChallengeSeven.passes()
}
