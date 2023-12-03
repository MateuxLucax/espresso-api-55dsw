package dev.mateux.espresso.domain.method

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BrewMethodRepository: JpaRepository<BrewMethod, Long> {
    @Query("""
        SELECT *,
               similarity(title, :title) AS similarity
          FROM brew_method
         WHERE similarity(title, :title) > 0.3
      ORDER BY similarity DESC
         LIMIT 1
    """, nativeQuery = true)
    fun getBySimilarityName(title: String): BrewMethod?
}