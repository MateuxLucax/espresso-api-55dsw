package dev.mateux.espresso.domain.recipe

import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository: JpaRepository<Recipe, Long>