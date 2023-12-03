package dev.mateux.espresso.service

import dev.mateux.espresso.domain.artisan.Artisan
import org.springframework.security.crypto.bcrypt.BCrypt

class HashService {
    companion object {
        fun validateArtisan(artisan: Artisan, password: String): Boolean {
            return try {
                BCrypt.checkpw(artisan.email + password + artisan.salt, artisan.hashedPassword)
            } catch (e: Exception) {
                false
            }
        }

        fun hashPassword(email: String, password: String, salt: String): String {
            return BCrypt.hashpw(email + password + salt, generateSalt())
        }

        fun generateSalt(): String {
            return BCrypt.gensalt()
        }
    }
}