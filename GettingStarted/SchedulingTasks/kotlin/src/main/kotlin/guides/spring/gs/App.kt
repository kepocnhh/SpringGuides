package guides.spring.gs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
