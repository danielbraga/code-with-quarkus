package org.acme.codewithquarkus.testcontainers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
public class PostgreSQLTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    @BeforeAll
    static void startContainer() {
        postgresContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        postgresContainer.stop();
    }

    @Test
    void testDatabaseConnection() throws Exception {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(255))");
            statement.execute("INSERT INTO users (name) VALUES ('Alice')");

            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                assertEquals(1, count, "A contagem de usuários deve ser 1");
            }
        }
    }
}