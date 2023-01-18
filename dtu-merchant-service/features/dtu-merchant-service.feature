Feature: DTU Merchant Service feature

  Scenario: Merchant Account Registration
    When a "MerchantAccRegisterReq" event for a merchant is received
    Then the "MerchantAccRegistered" event is sent with the same correlation id
    And the merchant account is registered

  Scenario: Get Merchant Account for Transfer
    Given there is a merchant registered
    When a "MerchantAccForTransferReq" event for a transfer is received
    Then a "MerchantAccResponse" event for a transfer is sent with the same correlation id
    And the merchant for a transfer is retrieved