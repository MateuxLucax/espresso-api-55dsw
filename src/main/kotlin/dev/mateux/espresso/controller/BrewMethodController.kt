package dev.mateux.espresso.controller

import dev.mateux.espresso.service.BrewMethodService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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