package dev.mateux.espresso

import dev.mateux.espresso.domain.artisan.Artisan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.core.Authentication

@SpringBootApplication
class EspressoApplication

fun main(args: Array<String>) {
    runApplication<EspressoApplication>(*args)
}

fun Authentication.toArtisan(): Artisan {
    return principal as Artisan
}