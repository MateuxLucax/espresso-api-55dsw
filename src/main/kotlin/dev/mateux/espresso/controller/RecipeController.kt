package dev.mateux.espresso.controller

import dev.mateux.espresso.dto.artisan.favorite.ArtisanFavoriteDTO
import dev.mateux.espresso.dto.recipe.CreateRecipeDTO
import dev.mateux.espresso.dto.recipe.RecipeDTO
import dev.mateux.espresso.dto.recipe.note.DeleteRecipeNoteDTO
import dev.mateux.espresso.dto.recipe.note.RecipeNoteDTO
import dev.mateux.espresso.service.RecipeService
import dev.mateux.espresso.toArtisan
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("recipe")
@Suppress("unused")
class RecipeController(
    private val recipeService: RecipeService
) {
    @PostMapping
    fun createRecipe(@RequestBody recipeDTO: CreateRecipeDTO, authentication: Authentication): RecipeDTO {
        val artisan = authentication.toArtisan()

        try {
            return recipeService.createRecipe(recipeDTO, artisan)
        } catch (e: Exception) {
            if (e.message == "Brew method not found") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Brew method not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to create recipe. Please try again."
                )
            }
        }
    }

    @GetMapping("{id}")
    fun getById(@PathVariable(required = true) id: String, authentication: Authentication): RecipeDTO {
        try {
            val artisanId = authentication.toArtisan().id ?: throw Exception("Artisan not found.")
            return recipeService.getById(id.toLong(), artisanId)
        } catch (e: Exception) {
            if (e.message == "Recipe not found.") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to get recipe. Please try again."
                )
            }
        }
    }

    @PutMapping("{id}/favorite")
    fun setRecipeAsFavorite(
        @PathVariable(required = true) id: String,
        authentication: Authentication
    ): ArtisanFavoriteDTO {
        try {
            return recipeService.setRecipeAsFavorite(id.toLong(), authentication.toArtisan())
        } catch (e: Exception) {
            if (e.message == "Recipe not found.") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to favorite recipe. Please try again."
                )
            }
        }
    }

    @DeleteMapping("{id}/favorite")
    fun unsetRecipeAsFavorite(
        @PathVariable(required = true) id: String,
        authentication: Authentication
    ): ArtisanFavoriteDTO {
        try {
            return recipeService.unsetRecipeAsFavorite(id.toLong(), authentication.toArtisan())
        } catch (e: Exception) {
            if (e.message == "Recipe not found.") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to unset recipe as favorite. Please try again."
                )
            }
        }
    }

    @GetMapping("all")
    fun getAll(authentication: Authentication): List<RecipeDTO> {
        try {
            val artisanId = authentication.toArtisan().id ?: throw Exception("Artisan not found.")
            return recipeService.getAll(artisanId)
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to get recipes. Please try again."
            )
        }
    }

    @GetMapping("mine")
    fun getMine(authentication: Authentication): List<RecipeDTO> {
        try {
            val artisanId = authentication.toArtisan().id ?: throw Exception("Artisan not found.")
            return recipeService.getRecipesFromArtisan(artisanId)
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to get recipes. Please try again."
            )
        }
    }

    @GetMapping("{id}/note")
    fun getRecipeNotes(
        @PathVariable(required = true) id: String,
        authentication: Authentication
    ): List<RecipeNoteDTO> {
        try {
            val artisanId = authentication.toArtisan().id ?: throw Exception("Artisan not found.")
            return recipeService.recipeNotes(id.toLong(), artisanId)
        } catch (e: Exception) {
            if (e.message == "Recipe not found.") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to get recipe notes. Please try again."
                )
            }
        }
    }

    @PostMapping("{id}/note")
    fun addRecipeNote(
        @RequestBody body: Map<String, String>,
        @PathVariable(required = true) id: String,
        authentication: Authentication
    ): RecipeNoteDTO {
        try {
            val artisanId = authentication.toArtisan().id ?: throw Exception("Artisan not found.")
            return recipeService.addRecipeNote(
                recipeId = id.toLong(),
                text = body["text"] ?: throw Exception("Text not found."),
                artisanId = artisanId
            )
        } catch (e: Exception) {
            if (e.message == "Recipe not found.") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to add recipe note. Please try again."
                )
            }
        }
    }

    @DeleteMapping("{id}/note/{noteId}")
    fun deleteRecipeNote(
        @PathVariable(required = true) id: String,
        @PathVariable(required = true) noteId: String,
        authentication: Authentication
    ): DeleteRecipeNoteDTO {
        try {
            val artisanId = authentication.toArtisan().id ?: throw Exception("Artisan not found.")
            return recipeService.deleteRecipeNote(
                recipeNoteId = noteId.toLong(),
                artisanId = artisanId,
                recipeId = id.toLong()
            )
        } catch (e: Exception) {
            if (e.message == "Recipe note not found.") {
                throw ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Recipe note not found. Please try again."
                )
            } else {
                throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unable to delete recipe note. Please try again."
                )
            }
        }
    }
}