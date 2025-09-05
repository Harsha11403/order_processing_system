package com.precize.project.order_processing_system.observe;

import com.precize.project.order_processing_system.domain.Order;
import com.precize.project.order_processing_system.domain.OrderStatus;
import com.precize.project.order_processing_system.events.Event;

public class AlertObserver implements OrderObserver {

	@Override
	public void onEvent(Event event, Order before, Order after) {
		// TODO Auto-generated method stub
		if(before == null || after == null)
		{
			return;
		}
        OrderStatus beforeStatus = before.getStatus();
        OrderStatus afterStatus = after.getStatus();
        if (beforeStatus != afterStatus) {
            if (afterStatus == OrderStatus.CANCELLED) {
                System.out.printf("ALERT: Sending alert for Order %s: Status changed to CANCELLED%n", after.getOrderId());
            } else if (afterStatus == OrderStatus.SHIPPED) {
                System.out.printf("ALERT: Sending alert for Order %s: Status changed to SHIPPED (date: %s)%n", after.getOrderId(), after.getShippingDate());
            }
        }		
	}

}
