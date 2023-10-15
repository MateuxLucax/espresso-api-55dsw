package dev.mateux.espresso.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
@Suppress("unused")
class BaseController (
    @Value("\${version}")
    private val version: String,
) {
    @GetMapping
    fun index(): Map<String, *> {
        return mapOf(
            "version" to version
        )
    }
}