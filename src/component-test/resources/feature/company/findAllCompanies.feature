Feature: find all companies
  Scenario: client makes a call to GET /companies
    Given an api "/api/companies"
    When client sends a get request
    Then client receives status code of 200
    And client receives a response body equals to "companies-body"