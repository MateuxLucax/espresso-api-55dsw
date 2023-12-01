package dev.mateux.espresso.domain.method

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BrewMethodRepository: JpaRepository<BrewMethod, Long> {
    @Query("""
        SELECT id,
               title
          FROM brew_method
         WHERE similarity(title, :name) > 0.3
      ORDER BY 3 DESC
         LIMIT 1
    """, nativeQuery = true)
    fun getBySimilarityName(name: String): BrewMethod?
}