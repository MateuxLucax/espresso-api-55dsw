package dev.mateux.espresso.domain.method

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class BrewMethodSeeder(
    @Autowired
    private val brewMethodRepository: BrewMethodRepository
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
            BrewMethod(title = "Melitta"),
        )
    }

    override fun run(vararg args: String) {
        val alreadySeeded = brewMethodRepository.findAll()
        if (alreadySeeded.isNotEmpty()) {
            return
        }

        brewMethodRepository.saveAll(brewMethods)
    }
}