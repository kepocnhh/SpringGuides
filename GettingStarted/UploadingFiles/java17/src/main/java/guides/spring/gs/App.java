package guides.spring.gs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class App {
    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner init(final StorageService storageService) {
        return (ignored) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
