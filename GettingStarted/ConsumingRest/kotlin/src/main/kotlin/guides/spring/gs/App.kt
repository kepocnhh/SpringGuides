package guides.spring.gs

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

class Value(val id: Long, val quote: String) {
    override fun toString(): String {
        return "Value(id=$id, quote=$quote)"
    }
}

class Quote(val type: String, val value: Value) {
    override fun toString(): String {
        return "Quote(type=$type, value=$value)"
    }
}

@SpringBootApplication
open class App {
    companion object {
        val log = LoggerFactory.getLogger(App::class.java)!!
    }

    @Bean
    open fun template(builder: RestTemplateBuilder) = builder.build()

    @Bean
    open fun run(template: RestTemplate) = CommandLineRunner {
        val quote = template.getForObject("https://quoters.apps.pcfone.io/api/random", Quote::class.java)
        checkNotNull(quote)
        log.info(quote.toString())
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
