Feature: Registration feature

  Scenario: Merchant registration
    Given an unregistered merchant with a valid accountId: "1234567890"
    When When the merchant registers with a valid accountId: "1234567890"
    Then the merchant is registered
    And the merchant can be retrieved by merchantId

  Scenario: Customer registration
    Given an unregistered customer with a valid accountId: "1234567890"
    When When the customer registers with a valid accountId: "1234567890"
    Then the customer is registered
    And the customer can be retrieved by customerId
