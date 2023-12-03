package dev.mateux.espresso.dto.recipe

import dev.mateux.espresso.dto.recipe.step.RecipeStepDTO

data class RecipeDTO(
    val id: String,
    val title: String,
    val description: String,
    val method: String,
    val cups: Int,
    val steps: List<RecipeStepDTO>,
    val public: Boolean,
    val owner: String,
    val favorite: Boolean? = false,
)
