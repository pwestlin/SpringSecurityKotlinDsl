package nu.westlin.spring.security.springsecuritykotlindsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@SpringBootApplication
class SpringSecurityKotlinDslApplication

@EnableWebSecurity
class KotlinSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http {
            httpBasic {}
            authorizeRequests {
                // The rules are evaluated in order, from top to bottom.
                // So if we were to put authorize("/**", permitAll) first, all requests to everything would pass without an user.
                authorize("/greetings/{greeting}", hasAuthority("ROLE_ADMIN"))
                authorize("/greetings", hasAuthority("ROLE_USER"))
                authorize("/**", permitAll)
            }
        }
    }
}

class GreetingRepository {
    var greeting: String = "Hello"
}

fun routes(greetingRepository: GreetingRepository) = router {

    GET("/") { request ->
        val username = request.principal().map { it.name }.orElse("anonymous")
        ServerResponse.ok().body(mapOf("greeting" to "${greetingRepository.greeting}, $username"))
    }

    GET("/greetings") { request ->
        request.principal().map { it.name }.map { ServerResponse.ok().body(mapOf("greeting" to "${greetingRepository.greeting}, $it")) }.orElseGet { ServerResponse.badRequest().build() }
    }

    GET("/greetings/{greeting}") { request ->
        greetingRepository.greeting = request.pathVariable("greeting")
        ServerResponse.ok().build()
    }
}


@Suppress("DEPRECATION")
fun main(args: Array<String>) {
    runApplication<SpringSecurityKotlinDslApplication>(*args) {
        addInitializers(beans {

            bean<GreetingRepository>()

            bean {
                fun user(user: String, pw: String, vararg roles: String) =
                    User.withDefaultPasswordEncoder().username(user).password(pw).roles(*roles).build()

                InMemoryUserDetailsManager(
                    user("user1", "user1pw", "USER"),
                    user("admin1", "admin1pw", "USER", "ADMIN")
                )
            }

            bean(::routes)
        })
    }
}