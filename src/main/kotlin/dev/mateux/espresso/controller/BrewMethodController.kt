package dev.mateux.espresso.controller

import dev.mateux.espresso.dto.method.BrewMethodDTO
import dev.mateux.espresso.service.BrewMethodService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("brew/methods")
@Suppress("unused")
class BrewMethodController(
    private val brewMethodService: BrewMethodService
) {
    @GetMapping
    fun createRecipe(): List<String> {
        return brewMethodService.getAll()
    }
}