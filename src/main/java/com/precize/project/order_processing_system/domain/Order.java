package com.precize.project.order_processing_system.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.precize.project.order_processing_system.events.Event;

public class Order {

	private final String orderId;
	private String customerId;
	private List<OrderItem> items;
	private BigDecimal totalAmount;
	private BigDecimal amountPaid = BigDecimal.ZERO;
	private OrderStatus status;
	private List<String> eventHistory = new ArrayList<>();
	private String shippingDate;
	
	public Order(String orderId, String customerId, List<OrderItem> items, BigDecimal totalAmount) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.items = items;
		this.totalAmount = totalAmount;
		this.status = OrderStatus.PENDING;
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
	
    public void addAmountPaid(java.math.BigDecimal amt) {
        this.amountPaid = this.amountPaid.add(amt);
    }	
	
	public BigDecimal getAmountPaid()
	{
		return amountPaid;
	}
	
	public OrderStatus getStatus()
	{
		return status;
	}
	
	public void setStatus(OrderStatus status)
	{
		this.status = status;
	}
	
	public List<String> getEventHistory()
	{
		return eventHistory;
	}
	
	public void appendToHistory(Event event)
	{
		this.eventHistory.add(event.getEventId() + ":" + event.getEventType());
	}
	
	public String getShippingDate()
	{
		return shippingDate;
	}
	
	public void setShippingDate(String date)
	{
		this.shippingDate = date;
	}
	
}
