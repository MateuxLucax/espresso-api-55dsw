package dev.mateux.espresso.domain.artisan

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ArtisanRepository: JpaRepository<Artisan, Long> {
    override fun findById(ic: Long): Optional<Artisan>

    fun findByEmail(email: String): Artisan?
}