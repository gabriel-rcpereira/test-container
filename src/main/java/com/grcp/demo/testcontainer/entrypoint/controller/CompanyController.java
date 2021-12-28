package com.grcp.demo.testcontainer.entrypoint.controller;

import com.grcp.demo.testcontainer.entity.Company;
import com.grcp.demo.testcontainer.entrypoint.model.CompanyRequest;
import com.grcp.demo.testcontainer.entrypoint.model.CompanyResponse;
import com.grcp.demo.testcontainer.repository.CompanyRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/api/companies")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        var companiesResponse = this.companyRepository.findAll().stream()
                .map(p -> new CompanyResponse(p.getId(), p.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(companiesResponse);
    }

    @GetMapping("/api/companies/names")
    public ResponseEntity<List<CompanyResponse>> getCompaniesByStartsWith(@RequestParam("p") String nameStarts) {
        var companiesResponse = this.companyRepository.findByNameStartsWith(nameStarts).stream()
                .map(p -> new CompanyResponse(p.getId(), p.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(companiesResponse);
    }

    @PostMapping("/api/companies")
    public ResponseEntity<Void> postNewCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        var newCompany = new Company(companyRequest.name());
        this.companyRepository.save(newCompany);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
