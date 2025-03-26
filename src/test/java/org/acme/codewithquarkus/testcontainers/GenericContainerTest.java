package org.acme.codewithquarkus.testcontainers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericContainerTest {

    private static final GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("nginx:latest"))
            .withExposedPorts(80);

    @BeforeAll
    static void startContainer() {
        container.start();
    }

    @AfterAll
    static void stopContainer() {
        container.stop();
    }

    @Test
    void testContainerRunning() {
        assertTrue(container.isRunning(), "O contêiner deve estar em execução");
        System.out.println("Container running at: " + container.getHost() + ":" + container.getFirstMappedPort());
    }
}