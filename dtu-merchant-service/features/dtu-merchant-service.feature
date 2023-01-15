Feature: DTU Merchant Service feature

  Scenario: Merchant Bank Account Assigned
    When a "MerchantBankAccRequested" event for a payment is received
    Then the "MerchantBankAccAssigned" event is sent
    And the payment gets a merchant bank account id

  Scenario: Amount for Payment Assigned
    When a "AmountRequested" event for a payment is received
    Then the "AmountAssigned" event is sent
    And the payment gets an amount assigned