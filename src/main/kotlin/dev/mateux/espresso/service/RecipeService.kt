package dev.mateux.espresso.service

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.method.BrewMethod
import dev.mateux.espresso.domain.method.BrewMethodRepository
import dev.mateux.espresso.domain.recipe.Recipe
import dev.mateux.espresso.domain.recipe.RecipeRepository
import dev.mateux.espresso.domain.recipe.step.RecipeStep
import dev.mateux.espresso.domain.recipe.step.RecipeStepRepository
import dev.mateux.espresso.dto.recipe.CreateRecipeDTO
import dev.mateux.espresso.dto.recipe.RecipeDTO
import dev.mateux.espresso.dto.recipe.step.RecipeStepDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeStepRepository: RecipeStepRepository,
    private val brewMethodRepository: BrewMethodRepository
) {
    fun getById(id: Long, artisanId: Long): RecipeDTO {
        val recipe = recipeRepository.findByIdAndArtisan(id, artisanId) ?: throw Exception("Recipe not found.")
        val steps = recipeStepRepository.findByRecipeId(id)

        return RecipeDTO(
            id = recipe.id.toString(),
            title = recipe.title,
            description = recipe.description,
            method = recipe.method.title,
            cups = recipe.servings,
            steps = steps.map { step ->
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

    fun getAll(artisanId: Long): List<RecipeDTO> {
        val recipes = recipeRepository.findAllPublicAndFromArtisan(artisanId)

        return recipes.map { recipe ->
            RecipeDTO(
                id = recipe.id.toString(),
                title = recipe.title,
                description = recipe.description,
                method = recipe.method.title,
                cups = recipe.servings,
                steps = listOf(),
                public = recipe.public,
                owner = recipe.owner.id.toString(),
            )
        }
    }

    fun createRecipe(createRecipeDTO: CreateRecipeDTO, artisan: Artisan): RecipeDTO {
        val recipe = recipeRepository.save(
            Recipe(
                title = createRecipeDTO.title,
                description = createRecipeDTO.description,
                servings = createRecipeDTO.cups,
                public = createRecipeDTO.public,
                method = getMethod(createRecipeDTO.method),
                owner = artisan,
            )
        )

        val steps = createRecipeDTO.steps.map { step ->
            val (quantity, quantityType) = extractQuantityAndType(step)

            RecipeStep(
                quantity = quantity,
                quantityType = quantityType,
                description = step,
                recipe = recipe
            )
        }

        recipeStepRepository.saveAll(steps)

        return RecipeDTO(
            id = recipe.id.toString(),
            title = recipe.title,
            description = recipe.description,
            method = recipe.method.title,
            cups = recipe.servings,
            steps = steps.map { step ->
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

    fun getMethod(method: String): BrewMethod {
        return brewMethodRepository.getBySimilarityName(method) ?: throw Exception("Brew method not found")
    }

    fun extractQuantityAndType(text: String): Pair<Float, String> {
        val regex = Regex("""(\d+)\s*(g|gram|grams|ml|L)""")
        val matchResult = regex.find(text)

        return if (matchResult != null) {
            val (quantity, quantityType) = matchResult.destructured
            Pair(quantity.toFloat(), quantityType.lowercase(Locale.getDefault()))
        } else {
            Pair(0.toFloat(), "")
        }
    }
}