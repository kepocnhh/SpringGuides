package guides.spring.gs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.Thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final long timeInMillis = 5_000L;

	@Scheduled(fixedRate = timeInMillis)
	public void fixedRate() {
		log.info("fixed rate start {}", dateFormat.format(new Date()));
		try {Thread.sleep(1_000);} catch (Throwable e) {/*ignored*/}
		log.info("fixed rate finish {}", dateFormat.format(new Date()));
	}

	@Scheduled(fixedDelay = timeInMillis)
	public void fixedDelay() {
		log.info("fixed delay start {}", dateFormat.format(new Date()));
		try {Thread.sleep(2_000);} catch (Throwable e) {/*ignored*/}
		log.info("fixed delay finish {}", dateFormat.format(new Date()));
	}
}
