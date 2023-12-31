package dev.mateux.espresso.controller

import dev.mateux.espresso.dto.auth.LoginDTO
import dev.mateux.espresso.dto.auth.LoginResponseDTO
import dev.mateux.espresso.dto.auth.RegisterDTO
import dev.mateux.espresso.service.ArtisanService
import dev.mateux.espresso.service.HashService
import dev.mateux.espresso.service.TokenService
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
    private val tokenService: TokenService
) {
    @PostMapping("login")
    fun login(@RequestBody payload: LoginDTO): LoginResponseDTO {
        val artisan = artisanService.getArtisanByEmail(payload.email)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Email not found. Please check your email and try again."
            )

        val isPasswordValid = HashService.validateArtisan(artisan, payload.password)

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
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        if (!emailRegex.matches(payload.email)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Email is invalid. Please try again."
            )
        }

        val artisan = artisanService.getArtisanByEmail(payload.email)
        if (artisan != null) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already exists. Please try again."
            )
        }

        val salt = HashService.generateSalt()
        val hashedPassword = HashService.hashPassword(payload.email, payload.password, salt)
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