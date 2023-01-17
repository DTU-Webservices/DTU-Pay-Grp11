Feature: DTU Merchant Service feature

  Scenario: Merchant Account Registration
    When a "MerchantAccRegisterReq" event for a merchant is received
    Then the "MerchantAccRegistered" event is sent with the same correlation id
    And the merchant account is registered

  Scenario: Get Merchant Account for Transfer
    When a "AmountRequested" event for a payment is received
    Then the "AmountAssigned" event is sent
    And the payment gets an amount assigned