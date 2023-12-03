package dev.mateux.espresso.seeders

import dev.mateux.espresso.domain.method.BrewMethod
import dev.mateux.espresso.domain.method.BrewMethodRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class BrewMethodSeeder(
    @Autowired
    private val repository: BrewMethodRepository
) : CommandLineRunner {
    companion object {
        val brewMethods = listOf(
            BrewMethod(title = "Aeropress"),
            BrewMethod(title = "Chemex"),
            BrewMethod(title = "French Press"),
            BrewMethod(title = "Pour Over"),
            BrewMethod(title = "Siphon"),
            BrewMethod(title = "Hario V60"),
            BrewMethod(title = "Kalita Wave"),
            BrewMethod(title = "Melitta")
        )
    }

    override fun run(vararg args: String) {
        val count = repository.count()
        if (count > 0) return

        repository.saveAll(brewMethods)
    }
}