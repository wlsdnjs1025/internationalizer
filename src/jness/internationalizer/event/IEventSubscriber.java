package jness.internationalizer.event;

public interface IEventSubscriber {
	public void update(EventType eventType, Object object);
}
