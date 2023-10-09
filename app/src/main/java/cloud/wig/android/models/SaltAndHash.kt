package cloud.wig.android.models

import android.util.Log
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATIONS = 120_000
private const val KEY_LENGTH = 256
private const val SECRET = "JesusIsKing"
class SaltAndHash {

    /**
     * generateHash generates the hashed password
     * @param password The users password
     * @param salt The generated salt
     * @return The generated hash
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun generateHash(password: String, salt: String): String {
        val combinedSalt = "$salt$SECRET".toByteArray()

        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        Log.d("generateHash", "complete")
        return hash.toHexString()
    }

    /**
     * generateSalt randomly generates a salt
     * @return ByteArray The salt
     */
    fun generateSalt() : ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)

        Log.d("generateSalt", "Complete")
        return salt
    }

}