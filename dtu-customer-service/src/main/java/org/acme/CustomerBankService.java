package org.acme;

import messaging.CorrelationId;
import messaging.Event;
import messaging.MessageQueue;
import org.acme.Repo.CustomerRepo;

public class CustomerBankService {

    private static final String CUSTOMER_ACC_REGISTER = "CustomerAccRegistered";
    private static final String CUSTOMER_GET_ACCOUNT = "CustomerAccGet";

    MessageQueue queue;

    public CustomerBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("CustomerAccRegisterReq", this::handleCustomerAccountRegister);
        this.queue.addHandler("CustomerAccGetReq", this::handleCustomerAccountGet);
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
        var customer= ev.getArgument(0, Customer.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        customer = CustomerRepo.getCustomer(customer.getCustomerId());
        Event event = new Event(CUSTOMER_GET_ACCOUNT, new Object[] { customer, correlationId });
        queue.publish(event);
    }

}