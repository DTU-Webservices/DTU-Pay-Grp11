package tokengeneration.service;
import java.util.UUID;
import messaging.Event;
import messaging.MessageQueue;


public class TokenIdService {
    private static final String TOKEN_ID_ASSIGNED = "TokenIdAssigned";
    private static final String TOKEN_REGISTRATION_REQUESTED = "TokenRegistrationRequested";

    static int id = 0;
    MessageQueue queue;

    public TokenIdService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler(TOKEN_REGISTRATION_REQUESTED, this::handleTokenRegistrationRequested);
    }

    private String generateTokenId() {
        id++;
        return Integer.toString(id);

    }

    public void handleTokenRegistrationRequested(Event e) {
        var t = e.getArgument(0, Token.class);
        var correlationid = e.getArgument(1, CorrelationId.class);
        t.setId(generateTokenId());
        Event event = new Event(TOKEN_ID_ASSIGNED, new Object[] { t, correlationid });
        queue.publish(event);
    }
}
