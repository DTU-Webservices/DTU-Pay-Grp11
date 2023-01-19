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

  Scenario: Get Merchant Id for Report
    Given there is a merchant with empty merchant id
    When a "ReportAllMerchantPayReq" event for a report is received
    Then a "MerchantIdGetResponse" event is sent for a merchant with merchant id assigned
    And the merchant has an id assigned

  Scenario: Delete Merchant Account
    Given there is a merchant registered with non-empty values
    When a "MerchantAccDeleteReq" event is received for a merchant account
    Then a "MerchantAccDeleteResponse" event is sent to delete
    And the merchant account is deleted
