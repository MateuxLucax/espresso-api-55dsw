package dev.mateux.espresso.service

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.artisan.favorite.ArtisanFavorite
import dev.mateux.espresso.domain.artisan.favorite.ArtisanFavoriteRepository
import dev.mateux.espresso.domain.method.BrewMethod
import dev.mateux.espresso.domain.method.BrewMethodRepository
import dev.mateux.espresso.domain.recipe.Recipe
import dev.mateux.espresso.domain.recipe.RecipeRepository
import dev.mateux.espresso.domain.recipe.note.RecipeNote
import dev.mateux.espresso.domain.recipe.note.RecipeNoteRepository
import dev.mateux.espresso.domain.recipe.step.RecipeStep
import dev.mateux.espresso.domain.recipe.step.RecipeStepRepository
import dev.mateux.espresso.dto.artisan.favorite.ArtisanFavoriteDTO
import dev.mateux.espresso.dto.recipe.CreateRecipeDTO
import dev.mateux.espresso.dto.recipe.DeleteRecipeDTO
import dev.mateux.espresso.dto.recipe.RecipeDTO
import dev.mateux.espresso.dto.recipe.note.DeleteRecipeNoteDTO
import dev.mateux.espresso.dto.recipe.note.RecipeNoteDTO
import dev.mateux.espresso.dto.recipe.step.RecipeStepDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeStepRepository: RecipeStepRepository,
    private val brewMethodRepository: BrewMethodRepository,
    private val artisanFavoriteRepository: ArtisanFavoriteRepository,
    private val recipeNoteRepository: RecipeNoteRepository
) {
    fun getById(id: Long, artisanId: Long): RecipeDTO {
        val recipe = recipeRepository.findByIdAndArtisan(id, artisanId) ?: throw Exception("Recipe not found.")
        val steps = recipeStepRepository.findByRecipeId(id)

        return RecipeDTO(id = recipe.id.toString(),
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
            favorite = artisanFavoriteRepository.findByArtisanIdAndRecipeId(artisanId, id) != null)
    }

    fun getAll(artisanId: Long): List<RecipeDTO> {
        val recipes = recipeRepository.findAllPublicAndFromArtisan(artisanId)

        return recipes.map { recipe ->
            RecipeDTO(id = recipe.id.toString(),
                title = recipe.title,
                description = recipe.description,
                method = recipe.method.title,
                cups = recipe.servings,
                steps = listOf(),
                public = recipe.public,
                owner = recipe.owner.id.toString(),
                favorite = recipe.id?.let {
                    artisanFavoriteRepository.findByArtisanIdAndRecipeId(
                        artisanId, it
                    ) != null
                })
        }
    }

    fun getRecipesFromArtisan(artisanId: Long): List<RecipeDTO> {
        val recipes = recipeRepository.findAllFromArtisan(artisanId)

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
                favorite = recipe.id?.let {
                    artisanFavoriteRepository.findByArtisanIdAndRecipeId(artisanId,
                        it
                    ) != null
                }
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
                quantity = quantity, quantityType = quantityType, description = step, recipe = recipe
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

    fun setRecipeAsFavorite(id: Long, artisan: Artisan): ArtisanFavoriteDTO {
        if (artisan.id == null) throw Exception("Artisan not found.")

        val recipe = recipeRepository.findByIdAndArtisan(id, artisan.id) ?: throw Exception("Recipe not found.")
        if (recipe.id == null) throw Exception("Recipe not found.")

        val alreadyFavorite = artisanFavoriteRepository.findByArtisanIdAndRecipeId(artisan.id, recipe.id) != null
        if (alreadyFavorite) return ArtisanFavoriteDTO(success = true)

        return artisanFavoriteRepository.save(
            ArtisanFavorite(
                artisan = artisan, recipe = recipe
            )
        ).let {
            ArtisanFavoriteDTO(
                success = true
            )
        }
    }

    fun unsetRecipeAsFavorite(id: Long, artisan: Artisan): ArtisanFavoriteDTO {
        if (artisan.id == null) throw Exception("Artisan not found.")

        val recipe = recipeRepository.findByIdAndArtisan(id, artisan.id) ?: throw Exception("Recipe not found.")
        if (recipe.id == null) throw Exception("Recipe not found.")

        if (artisanFavoriteRepository.findByArtisanIdAndRecipeId(artisan.id, recipe.id) == null) {
            return ArtisanFavoriteDTO(success = true)
        }

        val didRemove =  artisanFavoriteRepository.deleteByArtisanIdAndRecipeId(artisan.id, recipe.id)
        return ArtisanFavoriteDTO(success = didRemove == 1)
    }

    fun recipeNotes(recipeId: Long, artisanId: Long): List<RecipeNoteDTO> {
        val recipe = recipeRepository.findByIdAndArtisan(recipeId, artisanId) ?: throw Exception("Recipe not found.")
        if (recipe.id == null) throw Exception("Recipe not found.")

        val notes = recipeNoteRepository.findByRecipeAndArtisanId(recipeId, artisanId)

        return notes.map { note ->
            RecipeNoteDTO(
                id = note.id.toString(),
                text = note.text,
                owner = note.owner.id.toString(),
                recipe = note.recipe.id.toString(),
                createdAt = note.createdDate.toString()
            )
        }
    }

    fun addRecipeNote(recipeId: Long, text: String, artisanId: Long): RecipeNoteDTO {
        val recipe = recipeRepository.findByIdAndArtisan(recipeId, artisanId) ?: throw Exception("Recipe not found.")
        if (recipe.id == null) throw Exception("Recipe not found.")

        val note = recipeNoteRepository.save(
            RecipeNote(
                text = text, recipe = recipe, owner = recipe.owner
            )
        )

        return RecipeNoteDTO(
            id = note.id.toString(),
            text = note.text,
            owner = note.owner.id.toString(),
            recipe = note.recipe.id.toString(),
            createdAt = note.createdDate.toString()
        )
    }

    @Transactional
    fun deleteRecipeNote(recipeNoteId: Long, artisanId: Long, recipeId: Long): DeleteRecipeNoteDTO {
        recipeNoteRepository.findByIdArtisanIdRecipeId(recipeNoteId, artisanId, recipeId) ?: throw Exception("Recipe note not found.")

        recipeNoteRepository.deleteByIdArtisanIdRecipeId(recipeNoteId, artisanId, recipeId)
        return DeleteRecipeNoteDTO(success = true)
    }

    @Transactional
    fun deleteRecipe(recipeId: Long, artisanId: Long): DeleteRecipeDTO {
        val recipe = recipeRepository.findByIdAndArtisan(recipeId, artisanId) ?: throw Exception("Recipe not found.")
        if (recipe.id == null) throw Exception("Recipe not found.")

        recipeNoteRepository.deleteByRecipeId(recipeId)
        recipeStepRepository.deleteByRecipeId(recipeId)
        recipeRepository.deleteById(recipeId)
        return DeleteRecipeDTO(success = true)
    }

    @Transactional
    fun updateRecipe(recipeId: Long, artisanId: Long, dto: CreateRecipeDTO): RecipeDTO {
        val recipe = recipeRepository.findByIdAndArtisan(recipeId, artisanId) ?: throw Exception("Recipe not found.")
        if (recipe.id == null) throw Exception("Recipe not found.")

        val updatedRecipe = recipe.copy(
            title = dto.title,
            description = dto.description,
            servings = dto.cups,
            public = dto.public,
            method = getMethod(dto.method),
        )

        recipeRepository.save(updatedRecipe)

        recipeStepRepository.deleteByRecipeId(recipeId)

        val steps = dto.steps.map { step ->
            val (quantity, quantityType) = extractQuantityAndType(step)

            RecipeStep(
                quantity = quantity, quantityType = quantityType, description = step, recipe = recipe
            )
        }

        recipeStepRepository.saveAll(steps)

        return RecipeDTO(
            id = updatedRecipe.id.toString(),
            title = updatedRecipe.title,
            description = updatedRecipe.description,
            method = updatedRecipe.method.title,
            cups = updatedRecipe.servings,
            steps = steps.map { step ->
                RecipeStepDTO(
                    id = step.id.toString(),
                    description = step.description,
                    quantity = step.quantity,
                    unit = step.quantityType,
                )
            },
            public = updatedRecipe.public,
            owner = updatedRecipe.owner.id.toString(),
            favorite = artisanFavoriteRepository.findByArtisanIdAndRecipeId(artisanId, recipeId) != null
        )
    }
}