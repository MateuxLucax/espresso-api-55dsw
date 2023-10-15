package dev.mateux.espresso.controller

import dev.mateux.espresso.dto.ArtisanDTO
import dev.mateux.espresso.toArtisan
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("artisan")
@Suppress("unused")
class ArtisanController {
    @GetMapping("me")
    fun me(authentication: Authentication): ArtisanDTO {
        val artisan = authentication.toArtisan()
        return ArtisanDTO(
            id = artisan.id.toString(),
            name = artisan.name,
            email = artisan.email
        )
    }
}
