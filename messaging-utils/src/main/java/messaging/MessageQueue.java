package messaging;

import java.util.function.Consumer;

/**
 * @Source Hubert Baumeister Correlation Example provided in 02267
 */
public interface MessageQueue {

	void publish(Event message);
	void addHandler(String eventType, Consumer<Event> handler);

}
