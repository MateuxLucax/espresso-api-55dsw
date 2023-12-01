package dev.mateux.espresso.service

import dev.mateux.espresso.domain.method.BrewMethodRepository
import org.springframework.stereotype.Service

@Service
class BrewMethodService(
    private val brewMethodRepository: BrewMethodRepository
) {
    fun getAll(): List<String> {
        return brewMethodRepository.findAll().map { brewMethod -> brewMethod.title }
    }
}