package dev.mateux.espresso.domain.recipe.step

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity(name = "recipe_step")
class RecipeStep(
    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    val id: UUID,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    val name: String,

    @Column(nullable = false)
    @Lob
    val description: String,

    @Column(nullable = false, name = "step_number")
    val stepNumber: Int,

    /**
     * Quantity and QuantityType are nullable because it is not a given. We will try to find this values in the description with regex or another algorithm.
     * Like: "1 cup of flour" or "1 cup flour" then we will extract the quantity and the quantity type as quantity: 1, quantityType: cup
     * If we can't find it, we will set it to null and the frontend will have to handle it.
     */
    @Column(nullable = true)
    val quantity: Float,

    @Column(nullable = true, name = "quantity_type")
    val quantityType: String,

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    val recipe: Recipe,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (other !is Artisan) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}