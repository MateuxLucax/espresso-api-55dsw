package dev.mateux.espresso.domain.artisan.favorite

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity(name = "artisan_favorite")
class ArtisanFavorite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id", referencedColumnName = "id", nullable = false, updatable = false)
    val artisan: Artisan,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false, updatable = false)
    val recipe: Recipe,

    @Column(nullable = false, name = "created_at")
    @CreatedDate
    val createdDate: Instant? = Instant.now(),

    @Column(nullable = false, name = "updated_at")
    @LastModifiedDate
    val updatedAt: Instant? = Instant.now()
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