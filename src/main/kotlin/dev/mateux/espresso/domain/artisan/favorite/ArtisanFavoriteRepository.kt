package dev.mateux.espresso.domain.artisan.favorite

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface ArtisanFavoriteRepository: JpaRepository<ArtisanFavorite, Long> {
    fun findByArtisanIdAndRecipeId(artisanId: Long, recipeId: Long): ArtisanFavorite?

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM artisan_favorite
              WHERE artisan_id = :artisanId
	            AND recipe_id = :recipeId
    """, nativeQuery = true)
    fun deleteByArtisanIdAndRecipeId(artisanId: Long, recipeId: Long): Int
}