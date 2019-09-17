package jness.internationalization.event;

public interface IEventSubscriber {
	public void update(EventType eventType, Object object);
}
