package dev.mateux.espresso.controller

import dev.mateux.espresso.dto.recipe.RecipeDTO
import dev.mateux.espresso.service.PublicService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("public")
@Suppress("unused")
class PublicController(
    val publicService: PublicService,
) {
    @GetMapping("recipe/{id}")
    fun getById(@PathVariable(required = true) id: String): RecipeDTO {
        try {
            return publicService.getRecipeById(id.toLong())
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

    @GetMapping("recipe/all")
    fun getAll(): List<RecipeDTO> {
        try {
            return publicService.getAllRecipes()
        } catch (e: Exception) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Unable to get recipes. Please try again."
            )
        }
    }
}