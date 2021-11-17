package guides.spring.gs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class App implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(final String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("""
            CREATE TABLE customers(
                id SERIAL,
                first_name VARCHAR(255),
                last_name VARCHAR(255)
            )""".stripIndent());
        final var names = Stream.of("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
                .map(it -> (Object[]) it.split(" ")).collect(Collectors.toUnmodifiableList());
        names.forEach(it -> log.info(String.format("Inserting customer record for %s %s", it[0], it[1])));
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", names);
        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
            "SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
            (set, ignored) -> new Customer(
                set.getLong("id"),
                set.getString("first_name"),
                set.getString("last_name")
            ),
            new Object[] { "Josh" }
        ).forEach(it -> log.info(it.toString()));
        // expected "Josh Bloch" and "Josh Long"
    }
}
