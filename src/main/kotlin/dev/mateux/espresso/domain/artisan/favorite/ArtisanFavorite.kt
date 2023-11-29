package dev.mateux.espresso.domain.artisan.favorite

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.sql.Timestamp
import java.time.Instant
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

    @Column(nullable = false, name = "created_at")
    @CreatedDate
    val createdDate: Instant,

    @Column(nullable = false, name = "updated_at")
    @LastModifiedDate
    val updatedAt: Instant,
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