package com.precize.project.order_processing_system.events;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentReceivedEvent extends Event {

	private String orderId;
	private BigDecimal amountPaid;
	
	
	
	public PaymentReceivedEvent() {
		super();
	}

	@JsonCreator
	public PaymentReceivedEvent(@JsonProperty("eventId") String eventId,
								@JsonProperty("timeStamp") Instant timeStamp,
								@JsonProperty("eventType") String eventType,
								@JsonProperty("orderId") String orderId,
								@JsonProperty("amountPaid") BigDecimal amountPaid)
	{
		super(eventId, timeStamp, eventType);
		this.orderId = orderId;
		this.amountPaid = amountPaid;
	}
	
	public String getOrderId()
	{
		return orderId;
	}
	
	public BigDecimal getAmountPaid()
	{
		return amountPaid;
	}
}
