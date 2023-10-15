package dev.mateux.espresso.controller

import dev.mateux.espresso.service.TokenService
import dev.mateux.espresso.dto.LoginDTO
import dev.mateux.espresso.dto.LoginResponseDTO
import dev.mateux.espresso.dto.RegisterDTO
import dev.mateux.espresso.service.ArtisanService
import dev.mateux.espresso.service.HashService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("auth")
@Suppress("unused")
class AuthController(
    private val artisanService: ArtisanService,
    private val hashService: HashService,
    private val tokenService: TokenService
) {
    @PostMapping("login")
    fun login(@RequestBody payload: LoginDTO): LoginResponseDTO {
        val artisan = artisanService.getArtisanByEmail(payload.email)
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Email not found. Please check your email and try again."
            )

        val isPasswordValid = hashService.validateArtisan(artisan, payload.password)

        if (isPasswordValid) {
            return LoginResponseDTO(
                token = tokenService.createToken(artisan)
            )
        }

        throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Password is incorrect. Please check your password and try again."
        )
    }

    @PostMapping("register")
    fun register(@RequestBody payload: RegisterDTO): LoginResponseDTO {
        val artisan = artisanService.getArtisanByEmail(payload.email)

        if (artisan != null) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already exists. Please try again."
            )
        }

        val salt = hashService.generateSalt()
        val hashedPassword = hashService.hashPassword(payload.email, payload.password, salt)

        val newArtisan = artisanService.createArtisan(
            email = payload.email,
            password = hashedPassword,
            name = payload.name,
            salt = salt
        )

        return LoginResponseDTO(
            token = tokenService.createToken(newArtisan)
        )
    }
}