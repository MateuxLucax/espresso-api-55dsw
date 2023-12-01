package dev.mateux.espresso.domain.recipe.step

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecipeStepRepository: JpaRepository<RecipeStep, Long> {
    @Query("SELECT * FROM recipe_step WHERE recipe_id = :recipeId", nativeQuery = true)
    fun findByRecipeId(recipeId: Long): List<RecipeStep>
}