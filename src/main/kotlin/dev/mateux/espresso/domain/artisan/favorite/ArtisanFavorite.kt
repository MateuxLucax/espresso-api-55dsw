package dev.mateux.espresso.domain.artisan.favorite

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import java.util.*

@Entity(name = "artisan_favorite")
class ArtisanFavorite(
    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "artisan_id")
    val artisan: Artisan,

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    val recipe: Recipe,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArtisanFavorite

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}