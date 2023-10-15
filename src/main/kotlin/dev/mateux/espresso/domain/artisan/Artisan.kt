package dev.mateux.espresso.domain.artisan

import dev.mateux.espresso.domain.recipe.Recipe
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
class Artisan(
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(updatable = false, unique = true, nullable = false)
    val id: UUID,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    val name: String,

    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 255, message = "Email must be between 2 and 255 characters")
    val email: String,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Password must be between 2 and 255 characters")
    val hashedPassword: String,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Salt must be between 2 and 255 characters")
    val salt: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "artisan_favorite_recipe",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "recipe_id")]
    )
    val favoriteRecipes: Set<Recipe> = emptySet(),
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(
            SimpleGrantedAuthority("USER")
        )
    }

    override fun getPassword(): String {
        return hashedPassword
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

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
