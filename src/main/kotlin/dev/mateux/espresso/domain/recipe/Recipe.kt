package dev.mateux.espresso.domain.recipe

import dev.mateux.espresso.domain.artisan.Artisan
import dev.mateux.espresso.domain.method.BrewMethod
import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    val id: Long? = null,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    val public: Boolean,

    @ManyToOne
    @JoinColumn(name = "brew_method_id", referencedColumnName = "id", nullable = false, updatable = false)
    val method: BrewMethod,

    @Column(nullable = false)
    val servings: Int,

    @ManyToOne
    @JoinColumn(name = "artisan_id", referencedColumnName = "id", nullable = false, updatable = false)
    val owner: Artisan,
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
