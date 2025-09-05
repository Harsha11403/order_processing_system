package com.precize.project.order_processing_system.events;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCancelledEvent extends Event {

	private String orderId;
	private String reason;
	
	
    public OrderCancelledEvent() {
		super();
	}

	@JsonCreator
    public OrderCancelledEvent(@JsonProperty("eventId") String eventId,
                               @JsonProperty("timestamp") Instant timestamp,
                               @JsonProperty("eventType") String eventType,
                               @JsonProperty("orderId") String orderId,
                               @JsonProperty("reason") String reason) {
        super(eventId, timestamp, eventType);
        this.orderId = orderId;
        this.reason = reason;
    }
    
    public String getOrderId()
    {
    	return orderId;    	
    }
    
    public String reason()
    {
    	return reason;
    }
}
