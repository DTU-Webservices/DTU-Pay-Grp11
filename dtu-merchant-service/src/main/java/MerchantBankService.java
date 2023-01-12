import messaging.Event;
import messaging.MessageQueue;
import org.acme.Payment;

public class MerchantBankService {

    MessageQueue queue;

    public MerchantBankService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("MerchantBankAccRequested", this::handleMerchantBankAccRequested);
        this.queue.addHandler("AmountRequested", this::handleAmountRequested);
    }

    public void handleMerchantBankAccRequested(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        //temporary value
        p.setMid("123");
        Event event = new Event("MerchantBankAccAssigned", new Object[] { p });
        queue.publish(event);
    }

    public void handleAmountRequested(Event ev) {
        var p = ev.getArgument(0, Payment.class);
        //temporary value
        p.setAmount("10");
        Event event = new Event("AmountAssigned", new Object[] { p });
        queue.publish(event);
    }
}
