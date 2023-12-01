package dev.mateux.espresso.config

import dev.mateux.espresso.service.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Suppress("unused")
class ApplicationSecurity (
    private val tokenService: TokenService,
) {
    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { it
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated()
        }.oauth2ResourceServer { oauthCustomizer ->
            oauthCustomizer.jwt { it
                .authenticationManager { auth ->
                    val jwt = auth as BearerTokenAuthenticationToken
                    val user = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")
                    UsernamePasswordAuthenticationToken(user, "", listOf(SimpleGrantedAuthority("USER")))
                }
            }
        }
        .cors(Customizer.withDefaults())
        .csrf {
            it.disable()
        }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .headers { it
            .frameOptions { option ->
                option.disable()
            }.xssProtection { option ->
                option.disable()
            }
        }
        .exceptionHandling { it
            .authenticationEntryPoint { _, response, _ ->
                response.sendError(401, "Unauthorized")
            }
            .accessDeniedHandler { _, response, _ ->
                response.sendError(403, "Forbidden")
            }
        }

        return http.build()
    }
}