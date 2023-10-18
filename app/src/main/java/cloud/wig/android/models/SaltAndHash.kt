package cloud.wig.android.models

import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val ALGORITHM = "PBKDF2WithHmacSHA512"
private const val ITERATIONS = 120_000
private const val KEY_LENGTH = 256
private const val SECRET = "JesusIsKing"

/**
 * SaltAndHash is a utility class for generating hashed passwords using a salted approach.
 *
 * This class uses the PBKDF2 key derivation function with HMAC-SHA512 as the hash function.
 * It performs a specified number of iterations to derive a secret key from the password and salt.
 * The resulting key is then converted to a hexadecimal string for storage or comparison.
 *
 * @author Matthew McCaughey
 * @property ALGORITHM The algorithm used for key derivation.
 * @property ITERATIONS The number of iterations for key derivation.
 * @property KEY_LENGTH The length (in bits) of the derived key.
 * @property SECRET A secret string used for further salting.
 */
class SaltAndHash {

    /**
     * Generates a hash for the given [password] using the provided [salt].
     *
     * @param password The password to be hashed.
     * @param salt The salt used for the hashing process.
     * @return A hexadecimal string representing the derived key.
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun generateHash(password: String, salt: String): String {

        val combinedSalt = "$salt$SECRET".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded

        return hash.toHexString()
    }

    /**
     * Generates a random salt.
     *
     * @return A byte array representing the generated salt.
     */
    fun generateSalt() : ByteArray {

        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)

        return salt
    }

}