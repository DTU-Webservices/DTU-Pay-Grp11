package messaging;

import lombok.Value;

import java.util.UUID;

/**
 * @Source Hubert Baumeister Correlation Example provided in 02267
 */
@Value
public class CorrelationId {

    UUID id;
    public static CorrelationId randomId() {
        return new CorrelationId(UUID.randomUUID());
    }
}
