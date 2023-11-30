package dev.mateux.espresso.service

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.artisan.ArtisanRepository
import org.springframework.stereotype.Service

@Service
class ArtisanService(
    private val artisanRepository: ArtisanRepository
) {
    fun getById(id: Long): Artisan? = artisanRepository.findById(id).orElse(null)

    fun getArtisanByEmail(email: String) = artisanRepository.findByEmail(email)

    fun createArtisan(email: String, password: String, name: String, salt: String): Artisan {
        val artisan = Artisan(
            email = email,
            hashedPassword = password,
            name = name,
            salt = salt,
        )
        return artisanRepository.save(artisan)
    }

    fun getAllArtisans(): List<Artisan> = artisanRepository.findAll()
}