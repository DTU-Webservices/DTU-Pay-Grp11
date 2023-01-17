package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.CustomerRepo;

public class CustomerBankService {

    private static final String CUSTOMER_ACC_REGISTER = "CustomerAccRegistered";
    private static final String CUSTOMER_GET_ACCOUNT = "CustomerAccGet";
    private static final String CUSTOMER_ACCOUNT_RESPONSE = "CustomerAccResponse";
    private static final String CUSTOMER_ID_RESPONSE = "CustomerIdResponse";

    MessageQueue queue;

    public CustomerBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("CustomerAccRegisterReq", this::handleCustomerAccountRegister);
        this.queue.addHandler("CustomerAccGetReq", this::handleCustomerAccountGet);
        this.queue.addHandler("GetCustomerAccForTransferReq", this::handleCustomerAccountGetForTransfer);
        this.queue.addHandler("GetCustomerIdForTransferReq", this::handleCustomerIdGetForTransfer);
    }

    public void handleCustomerAccountRegister(Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        customer.setCustomerId(correlationId.getId());
        CustomerRepo.addCustomer(customer);
        Event event = new Event(CUSTOMER_ACC_REGISTER, new Object[] { customer, correlationId });
        queue.publish(event);
    }

    public void handleCustomerAccountGet(Event ev) {
        handleCustomerRequestsForDifferentQueues(CUSTOMER_GET_ACCOUNT, ev);
    }

    public void handleCustomerAccountGetForTransfer(Event ev) {
        handleCustomerRequestsForDifferentQueues(CUSTOMER_ACCOUNT_RESPONSE, ev);
    }

    public void handleCustomerIdGetForTransfer(Event ev) {
        handleCustomerRequestsForDifferentQueues(CUSTOMER_ID_RESPONSE, ev);
    }

    public void handleCustomerRequestsForDifferentQueues(String responseHandler, Event ev) {
        var customer = ev.getArgument(0, Customer.class);
        var token = customer.getCurrentToken();
        var correlationId = ev.getArgument(1, CorrelationId.class);
        customer = CustomerRepo.getCustomer(customer.getCustomerId());
        customer.setCurrentToken(token);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!CustomerBankService: " + customer);
        Event event = new Event(responseHandler, new Object[] { customer, correlationId });
        queue.publish(event);
    }

}