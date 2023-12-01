package dev.mateux.espresso.service
import dev.mateux.espresso.domain.recipe.RecipeRepository
import dev.mateux.espresso.domain.recipe.step.RecipeStepRepository
import dev.mateux.espresso.dto.recipe.RecipeDTO
import dev.mateux.espresso.dto.recipe.step.RecipeStepDTO
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class PublicService(
    private val recipeRepository: RecipeRepository,
    private val recipeStepRepository: RecipeStepRepository
) {
    fun getRecipeById(id: Long): RecipeDTO {
        val recipe = recipeRepository.findPublicById(id) ?: throw Exception("Recipe not found.")
        val recipeSteps = recipeStepRepository.findByRecipeId(id)

        return RecipeDTO(
            id = recipe.id.toString(),
            title = recipe.title,
            description = recipe.description,
            method = recipe.method.title,
            cups = recipe.servings,
            steps = recipeSteps.map { step ->
                RecipeStepDTO(
                    id = step.id.toString(),
                    description = step.description,
                    quantity = step.quantity,
                    unit = step.quantityType,
                )
            },
            public = recipe.public,
            owner = recipe.owner.id.toString(),
        )
    }
}