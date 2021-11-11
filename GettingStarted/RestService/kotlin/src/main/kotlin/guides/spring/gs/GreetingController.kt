package guides.spring.gs

import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController {
	companion object {
		val template = "Hello, %s!"
		val counter = AtomicLong(0)
	}

	@RequestMapping(method = arrayOf(RequestMethod.GET), value = arrayOf("/greeting"))
	fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
		return Greeting(counter.incrementAndGet(), String.format(template, name))
	}
}
