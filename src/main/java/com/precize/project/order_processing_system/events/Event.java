package com.precize.project.order_processing_system.events;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,
	    include = JsonTypeInfo.As.EXISTING_PROPERTY, // tells Jackson to look at "eventType"
	    property = "eventType",
	    visible = true
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = OrderCreatedEvent.class, name = "OrderCreated"),
	    @JsonSubTypes.Type(value = PaymentReceivedEvent.class, name = "PaymentReceived"),
	    @JsonSubTypes.Type(value = ShippingScheduledEvent.class, name = "ShippingScheduled"),
	    @JsonSubTypes.Type(value = OrderCancelledEvent.class, name = "OrderCancelled")
	})
public abstract class Event {

	private String eventId;
	private Instant timeStamp;
	private String eventType;
	
	public Event() {}

	public Event(String eventId, Instant timeStamp, String eventType) {
		super();
		this.eventId = eventId;
		this.timeStamp = timeStamp;
		this.eventType = eventType;
	}
	
	public String getEventId()
	{
		return eventId;
	}
	
	public Instant getTimeStamp()
	{
		return timeStamp;
	}
	
	public String getEventType()
	{
		return eventType;
	}
	
	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}
	
	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = this.timeStamp;
	}
	
	public void setEventType(String eventType)
	{
		this.eventType = eventType;
	}
}
