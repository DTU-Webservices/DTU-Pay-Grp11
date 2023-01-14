Feature: Registration feature

  Scenario: Merchant registration
    Given an unregistered merchant with a valid accountId: "1234567890"
    When When the merchant registers with a valid accountId: "1234567890"
    Then the merchant is registered
    And the merchant can be retrieved by merchantId