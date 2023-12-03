package dev.mateux.espresso.domain.artisan

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

@Entity
class Artisan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    val id: Long? = null,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    val name: String,

    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 255, message = "Email must be between 2 and 255 characters")
    val email: String,

    @Column(nullable = false, name = "hashed_password")
    @Size(min = 2, max = 255, message = "Password must be between 2 and 255 characters")
    val hashedPassword: String,

    @Column(nullable = false)
    @Size(min = 2, max = 255, message = "Salt must be between 2 and 255 characters")
    val salt: String,

    @Column(nullable = false, name = "created_at")
    @CreatedDate
    val createdDate: Instant? = Instant.now(),

    @Column(nullable = false, name = "updated_at")
    @LastModifiedDate
    val updatedAt: Instant? = Instant.now(),
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
