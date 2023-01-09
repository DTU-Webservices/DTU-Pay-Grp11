Feature: Payment
    Scenario: Successful Payment
        Given a customer with a bank account with balance 1000
        And that the customer is registered with DTU Pay
        Given a merchant with a bank account with balance 2000
        And that the merchant is registered with DTU Pay
        When the merchant initiates a payment for 100 kr by the customer
        Then the payment is successful
        And the balance of the customer at the bank is 900 kr
        And the balance of the merchant at the bank is 2100 kr
        Then delete all accounts

    Scenario: List of payments
        Given a successful payment of "10" kr from customer "cid1" to merchant "mid1"
        When the manager asks for at list of payments
        Then the list contains a list of payments where customer "cid1" paid "10" kr to merchant "mid1"

    Scenario: Customer is not known
        Given a customer with id "cid2"
        And a merchant with id "mid1"
        When the merchant initiates a payment for "10" kr by the customer
        Then the payment is not successful
        And an error message is returned saying "customer with id cid2 is unknown"

    Scenario: Merchant is not known
        Given a customer with id "cid1"
        And a merchant with id "mid2"
        When the merchant initiates a payment for "10" kr by the customer
        Then the payment is not successful
        And an error message is returned saying "merchant with id mid2 is unknown"
