package com.precize.project.order_processing_system.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.precize.project.order_processing_system.domain.OrderItem;

public class OrderCreatedEvent extends Event {

	private String orderId;
	private String customerId;
	private List<OrderItem> items;
	private BigDecimal totalAmount;
	
	
	
    public OrderCreatedEvent() {
		super();
	}

	@JsonCreator
    public OrderCreatedEvent(@JsonProperty("eventId") String eventId,
                             @JsonProperty("timestamp") Instant timestamp,
                             @JsonProperty("eventType") String eventType,
                             @JsonProperty("orderId") String orderId,
                             @JsonProperty("customerId") String customerId,
                             @JsonProperty("items") List<OrderItem> items,
                             @JsonProperty("totalAmount") BigDecimal totalAmount)
    {
    	super(eventId, timestamp, eventType);
    	this.orderId = orderId;
    	this.customerId = customerId;
    	this.items = items;
    	this.totalAmount = totalAmount;
    }
    
    public String getOrderId()
    {
    	return orderId;
    }
    
    public String getCustomerId()
    {
    	return customerId;
    }
    
    public List<OrderItem> getItems()
    {
    	return items;
    }
    
    public BigDecimal getTotalAmount()
    {
    	return totalAmount;
    }
    
}
