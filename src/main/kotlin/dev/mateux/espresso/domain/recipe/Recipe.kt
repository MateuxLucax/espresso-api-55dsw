package dev.mateux.espresso.domain.recipe

import dev.mateux.espresso.domain.artisan.Artisan
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import java.util.*

@Entity
class Recipe(
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

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    val public: Boolean,

    @ManyToOne
    @JoinColumn(name = "artisan_id")
    val owner: Artisan,

    @ManyToMany(mappedBy = "favoriteRecipes")
    val favorites: Set<Artisan>,
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
