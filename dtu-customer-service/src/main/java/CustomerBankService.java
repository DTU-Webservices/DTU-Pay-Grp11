import messaging.Event;
import messaging.MessageQueue;
import org.acme.Payment;

public class CustomerBankService {

    MessageQueue queue;

    public CustomerBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("CustomerBankAccRequested", this::handleCustomerBankAccRequested);
    }

    public void handleCustomerBankAccRequested(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        //temporary value
        p.setCid("321");
        Event event = new Event("CustomerBankAccAssigned", new Object[] { p });
        queue.publish(event);
    }
}