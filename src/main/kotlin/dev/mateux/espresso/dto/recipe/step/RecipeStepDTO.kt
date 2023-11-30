package dev.mateux.espresso.dto.recipe.step

data class RecipeStepDTO(
    val id: String,
    val description: String,
    val quantity: Float?,
    val unit: String?,
)
