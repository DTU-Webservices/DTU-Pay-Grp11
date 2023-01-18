Feature: Merchant Makes Payment

  Scenario: Merchant makes Payment
    Given Given a user with a cprNumber of "986532-1357" and a firstName of "Paul" and a LastName of "Paulsen"
    And a user with a cprNumber of "986111-1357" and a firstName of "Hanne" and a LastName of "Hansen"
    When they are registered with their bank
    Then Paul can register in DTU Pay as a Merchant with his accountId with a balance of 1000
    And Hanne can register in DTU Pay as a Customer with her accountId with a balance of 5000
    Then Hanne can generate 5 tokens
    And Paul can get a token from Hanne
    Then Paul can make a payment of 100 from Hanne
    Then Hanne can see her balance of 4900
    Then Paul can see his balance of 1100
