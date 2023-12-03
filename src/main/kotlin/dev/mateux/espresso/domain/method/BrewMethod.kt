package dev.mateux.espresso.domain.method

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.Instant

@Entity
class BrewMethod(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true, nullable = false)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 255)
    val title: String,

    @CreatedDate
    @Column(nullable = false, name = "created_at")
    val createdDate: Instant? = Instant.now(),

    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    val updatedAt:  Instant? = Instant.now(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BrewMethod

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}