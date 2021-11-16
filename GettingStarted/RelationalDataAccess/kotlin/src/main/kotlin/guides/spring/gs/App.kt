package guides.spring.gs

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate

class Customer(val id: Long, val firstName: String, val lastName: String) {
    override fun toString(): String {
        return "Customer(id=$id,f=$firstName,l=$lastName)"
    }
}

@SpringBootApplication
open class App : CommandLineRunner {
    companion object {
        val log = LoggerFactory.getLogger(App::class.java)!!
    }

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    override fun run(vararg args: String?) {
        log.info("Creating tables")
        jdbcTemplate.execute("DROP TABLE customers IF EXISTS")
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))")
        val names: List<Array<Any>> = listOf("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
            .map { it.split(" ").toTypedArray() }
        names.forEach {
            log.info(String.format("Inserting customer record for %s %s", it[0], it[1]))
        }
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", names)
        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
            "SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
            { set, _ ->
                Customer(
                    id = set.getLong("id"),
                    firstName = set.getString("first_name"),
                    lastName = set.getString("last_name")
                )
            },
            arrayOf("Josh")
        ).forEach { log.info(it.toString()) }
        // expected "Josh Bloch" and "Josh Long"
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
