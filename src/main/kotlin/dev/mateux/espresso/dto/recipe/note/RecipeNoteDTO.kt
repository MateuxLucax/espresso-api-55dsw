package dev.mateux.espresso.dto.recipe.note

data class RecipeNoteDTO(
    val id: String,
    val text: String,
    val owner: String,
    val recipe: String,
    val createdAt: String
)
