package jness.internationalizer.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum EventManager {
	INSTANCE;
	
	private static final Map<EventType, List<IEventSubscriber>> subscribers = new HashMap<>();
	
	public static void add(EventType eventType, IEventSubscriber subscriber) {
		List<IEventSubscriber> list = subscribers.get(eventType);
		
		if (list != null) {
			list.add(subscriber);
			return;
		}

		List<IEventSubscriber> newList = new ArrayList<>();
		newList.add(subscriber);
		subscribers.put(eventType, newList);

	}
	
	public static void remove(EventType eventType, IEventSubscriber subscriber) {
		List<IEventSubscriber> list = subscribers.get(eventType);
		
		if (list != null) {
			list.remove(subscriber);
		}
	}
	
	public static void pushEvent(EventType eventType, Object object) {
		for (IEventSubscriber subscriber : subscribers.get(eventType)) {
			subscriber.update(eventType, object);
		}
	}
}
