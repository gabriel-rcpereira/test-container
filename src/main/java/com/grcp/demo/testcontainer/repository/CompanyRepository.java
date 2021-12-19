package com.grcp.demo.testcontainer.repository;

import com.grcp.demo.testcontainer.entity.Company;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

    List<Company> findAll();

    List<Company> findByNameStartsWith(String startsWith);
}
