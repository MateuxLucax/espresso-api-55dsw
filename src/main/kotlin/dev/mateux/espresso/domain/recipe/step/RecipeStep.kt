package dev.mateux.espresso.domain.recipe.step

import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity(name = "recipe_step")
class RecipeStep(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    val id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = true)
    val quantity: Float? = null,

    @Column(nullable = true, name = "quantity_type")
    val quantityType: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false, updatable = false)
    val recipe: Recipe,

    @Column(nullable = false, name = "created_at")
    @CreatedDate
    val createdDate: Instant? = Instant.now(),

    @Column(nullable = false, name = "updated_at")
    @LastModifiedDate
    val updatedAt: Instant? = Instant.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (other !is RecipeStep) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}