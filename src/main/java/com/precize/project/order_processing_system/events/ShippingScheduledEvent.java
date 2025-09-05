package com.precize.project.order_processing_system.events;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippingScheduledEvent extends Event {

	private String orderId;
	private LocalDate shippingDate;
	
    public ShippingScheduledEvent() {
		super();
	}

	@JsonCreator
    public ShippingScheduledEvent(@JsonProperty("eventId") String eventId,
                                  @JsonProperty("timestamp") Instant timestamp,
                                  @JsonProperty("eventType") String eventType,
                                  @JsonProperty("orderId") String orderId,
                                  @JsonProperty("shippingDate") LocalDate shippingDate) {
        super(eventId, timestamp, eventType);
        this.orderId = orderId;
        this.shippingDate = shippingDate;
    }	
    
    public String getOrderId()
    {
    	return orderId;
    }
    
    public LocalDate getShippingDate()
    {
    	return shippingDate;
    }
}
