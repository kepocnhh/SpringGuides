package guides.spring.gs

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
open class App {
    @Bean
    open fun run(service: StorageService) = CommandLineRunner {
        service.deleteAll()
        service.init()
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
