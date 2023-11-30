package dev.mateux.espresso.dto.recipe

class CreateRecipeDTO(
    val title: String,
    val description: String,
    val method: String,
    val cups: Int,
    val steps: List<String>,
    val public: Boolean,
)