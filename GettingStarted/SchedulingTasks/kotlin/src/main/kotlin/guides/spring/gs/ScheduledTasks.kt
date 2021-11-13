package guides.spring.gs

import java.text.SimpleDateFormat
import java.util.Date
import java.lang.Thread
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks {
	companion object {
		val log = LoggerFactory.getLogger(ScheduledTasks::class.java)
		val dateFormat = SimpleDateFormat("HH:mm:ss")
		const val timeInMillis = 5_000L
	}

	@Scheduled(fixedRate = timeInMillis)
	fun fixedRate() {
		log.info("fixed rate start {}", dateFormat.format(Date()))
		Thread.sleep(1_000)
		log.info("fixed rate finish {}", dateFormat.format(Date()))
	}

	@Scheduled(fixedDelay = timeInMillis)
	fun fixedDelay() {
		log.info("fixed delay start {}", dateFormat.format(Date()))
		Thread.sleep(2_000)
		log.info("fixed delay finish {}", dateFormat.format(Date()))
	}
}
