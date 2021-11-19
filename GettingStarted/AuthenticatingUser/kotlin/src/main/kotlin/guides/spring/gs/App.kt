package guides.spring.gs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Configuration
open class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        if (http == null) TODO()
        http.authorizeRequests()
            .anyRequest().fullyAuthenticated()
            .and()
            .formLogin()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        if (auth == null) TODO()
        auth.ldapAuthentication()
            .userDnPatterns("uid={0},ou=people")
            .groupSearchBase("ou=groups")
            .contextSource()
            .url("ldap://localhost:8389/dc=springframework,dc=org")
            .and()
            .passwordCompare()
            .passwordEncoder(BCryptPasswordEncoder())
            .passwordAttribute("userPassword")
    }
}

@RestController
class HomeController {
    @RequestMapping(method = [RequestMethod.GET], value = ["/"])
    fun index(): String {
        return "Welcome to the home page!"
    }
}

@SpringBootApplication
open class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
