package dev.mateux.espresso.domain.recipe

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecipeRepository: JpaRepository<Recipe, Long> {
    @Query("""
        SELECT *
          FROM recipe
         WHERE id = :id
           AND ((public = true) OR (public = false AND artisan_id = :artisanId))
    """, nativeQuery = true)
    fun findByIdAndArtisan(id: Long, artisanId: Long): Recipe?

    @Query("""
        SELECT *
          FROM recipe
         WHERE id = :id
           AND public = true
    """, nativeQuery = true)
    fun findPublicById(id: Long): Recipe?

    @Query("""
        SELECT *
          FROM recipe
         WHERE artisan_id = :artisanId
           AND ((public = true) OR (public = false AND artisan_id = :artisanId))
    """, nativeQuery = true)
    fun findAllPublicAndFromArtisan(artisanId: Long): List<Recipe>

    @Query("""
        SELECT *
          FROM recipe
         WHERE artisan_id = :artisanId
    """, nativeQuery = true)
    fun findAllFromArtisan(artisanId: Long): List<Recipe>

    @Query("""
        SELECT *
          FROM recipe
         WHERE public = true
    """, nativeQuery = true)
    fun findAllPublic(): List<Recipe>
}