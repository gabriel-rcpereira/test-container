package com.grcp.demo.testcontainer.repository;

import com.grcp.demo.testcontainer.entity.Company;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("integration-test")
public class CompanyRepositoryIT {

    private final DockerImageName dockerImageName = DockerImageName.parse("postgres:14.0");
    private static PostgreSQLContainer postgreSQLContainer;

    @Autowired
    private CompanyRepository companyRepository;

    public CompanyRepositoryIT() {
        postgreSQLContainer = new PostgreSQLContainer(dockerImageName);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    public void testIt() {
        companyRepository.save(new Company("company 1"));
        companyRepository.save(new Company("company 2"));
        companyRepository.save(new Company("company 3"));
        companyRepository.save(new Company("s company 3"));

        List<Company> companies = companyRepository.findByNameStartsWith("c");

        Assert.assertEquals(3, companies.size());
    }
}
