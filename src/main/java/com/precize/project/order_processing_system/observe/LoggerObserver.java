package com.precize.project.order_processing_system.observe;

import com.precize.project.order_processing_system.domain.Order;
import com.precize.project.order_processing_system.events.Event;
import com.precize.project.order_processing_system.events.OrderCancelledEvent;
import com.precize.project.order_processing_system.events.OrderCreatedEvent;
import com.precize.project.order_processing_system.events.PaymentReceivedEvent;
import com.precize.project.order_processing_system.events.ShippingScheduledEvent;

public class LoggerObserver implements OrderObserver {

	@Override
	public void onEvent(Event event, Order before, Order after) {
        String orderId = extractOrderId(event);
        String beforeStatus = before == null ? "null" : String.valueOf(before.getStatus());
        String afterStatus = after == null ? "null" : String.valueOf(after.getStatus());
        System.out.printf("INFO: Processed %s for %s; status %s -> %s%n", event.getEventType(), orderId, beforeStatus, afterStatus);		

	}
	
    private String extractOrderId(Event event) {
        try {
            
            if (event instanceof OrderCreatedEvent) {
                return ((OrderCreatedEvent) event).getOrderId();
            } else if (event instanceof PaymentReceivedEvent) {
                return ((PaymentReceivedEvent) event).getOrderId();
            } else if (event instanceof ShippingScheduledEvent) {
                return ((ShippingScheduledEvent) event).getOrderId();
            } else if (event instanceof OrderCancelledEvent) {
                return ((OrderCancelledEvent) event).getOrderId();
            }
        } catch (Exception ignored) {}
        return "UNKNOWN";
    }	

}
