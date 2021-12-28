package com.grcp.testcontainer;

import com.grcp.demo.testcontainer.Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("component-test")
@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApplicationTestContext {

    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("postgres:14.0");
    private static PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    @LocalServerPort
    protected int port;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer(DOCKER_IMAGE_NAME);
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }
}
