package dev.mateux.espresso.domain.recipe.note

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import java.util.*

@Entity(name = "recipe_note")
class RecipeNote(
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    val id: UUID,

    @Column(nullable = false)
    @Lob
    val text: String,

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    val recipe: Recipe,

    @ManyToOne
    @JoinColumn(name = "artisan_id")
    val owner: Artisan,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecipeNote

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}