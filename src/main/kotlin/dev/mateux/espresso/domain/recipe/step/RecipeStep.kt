package dev.mateux.espresso.domain.recipe.step

import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*

@Entity(name = "recipe_step")
class RecipeStep(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    val id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    /**
     * Quantity and QuantityType are nullable because it is not a given. We will try to find this values in the description with regex or another algorithm.
     * Like: "1 cup of flour" or "1 cup flour" then we will extract the quantity and the quantity type as quantity: 1, quantityType: cup
     * If we can't find it, we will set it to null and the frontend will have to handle it.
     */
    @Column(nullable = true)
    val quantity: Float,

    @Column(nullable = true, name = "quantity_type")
    val quantityType: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false, updatable = false)
    val recipe: Recipe,
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