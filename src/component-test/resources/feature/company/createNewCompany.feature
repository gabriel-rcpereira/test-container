Feature: create new company
  Scenario Outline: client makes a call to POST /companies
    Given an api "/api/companies"
    When client sends a post request with body <body>
    Then client receives status code of <status>
    And clean up company created

  Examples:
    | body                      | status  |
    | "new-company"             | 201     |
    | "new-company-with-error"  | 400     |
