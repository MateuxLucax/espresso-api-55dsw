package dev.mateux.espresso.service

import dev.mateux.espresso.domain.artisan.Artisan
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val artisanService: ArtisanService,
) {
    fun createToken(artisan: Artisan): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(1L, ChronoUnit.DAYS))
            .subject(artisan.name)
            .claim("id", artisan.id)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun parseToken(token: String): Artisan? {
        return try {
            val jwt = jwtDecoder.decode(token)
            val artisanId = jwt.claims["id"].toString().toLong()
            artisanService.getById(artisanId)
        } catch (e: Exception) {
            null
        }
    }
}
