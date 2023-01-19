Feature: Report Service Feature

  @ReportingService
  Scenario: Report For All Payments
    Given there is one or more payments
    When a "ReportAllPay" event for All Payments is received
    Then a "ReportAllPayResp" event is sent with the same correlation id
    And the report is generated

  @ReportingService
  Scenario: Report For All Payments Made By Customer
    Given there is one or more payments made by customer
    When a "ReportAllCustomerPay" event for All Payments Made By customer is received
    Then a "ReportAllPayResp" event is sent with the same correlationId and customer report
    And the report for customer is generated

  @ReportingService
  Scenario: Report For All Payments Made By Merchant
    Given there is one or more payments made by merchant
    When a "ReportAllMerchantPay" event for All Payments Made By merchant is received
    Then a "ReportAllPayResp" event is sent with the same correlationId and merchant report
    And the report for merchant is generated