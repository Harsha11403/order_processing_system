package com.precize.project.order_processing_system.observe;

import com.precize.project.order_processing_system.domain.Order;
import com.precize.project.order_processing_system.events.Event;

public interface OrderObserver {

	void onEvent(Event event, Order before, Order after);
}
