package guides.spring.gs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@SpringBootApplication
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public RestTemplate template(final RestTemplateBuilder builder) {
        return builder.build();
    }

    private void onTemplate(final RestTemplate template) {
        final Quote quote = template.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
        Objects.requireNonNull(quote);
        log.info(quote.toString());
    }

    @Bean
    public CommandLineRunner run(final RestTemplate template) {
        return args -> onTemplate(template);
    }
}
