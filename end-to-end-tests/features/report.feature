Feature: Reports

  @Report
  Scenario: Manager wants a report for all Payments
    Given any manager that wants a report of all payments
    Then the manager has a report of all payments

  @Report
  Scenario: Customer wants a report of all their payments
    Given a customer that wants a report of all their payments
    Then the customer requests a report getting all their payments

  @Report
  Scenario: Merchant wants a report of all their payments
    Given a merchant that wants a report of all their payments
    Then the merchant requests a report getting all their payments