package dev.mateux.espresso.controller

import dev.mateux.espresso.dto.recipe.CreateRecipeDTO
import dev.mateux.espresso.dto.recipe.RecipeDTO
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
}