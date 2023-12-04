package dev.mateux.espresso.domain.recipe.note

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface RecipeNoteRepository: JpaRepository<RecipeNote, Long> {
    @Query("""
        SELECT *
          FROM recipe_note
         WHERE id = :id
           AND artisan_id = :artisanId
           AND recipe_id = :recipeId
      ORDER BY created_at DESC
    """, nativeQuery = true)
    fun findByIdArtisanIdRecipeId(id: Long, artisanId: Long, recipeId: Long): RecipeNote?

    @Query("""
        SELECT *
          FROM recipe_note
         WHERE artisan_id = :artisanId
           AND recipe_id = :recipeId
      ORDER BY created_at DESC
    """, nativeQuery = true)
    fun findByRecipeAndArtisanId(recipeId: Long, artisanId: Long): List<RecipeNote>

    @Modifying
    @Query("""
        DELETE FROM recipe_note
         WHERE recipe_id = :recipeId
    """, nativeQuery = true)
    fun deleteByRecipeId(recipeId: Long): Int
}