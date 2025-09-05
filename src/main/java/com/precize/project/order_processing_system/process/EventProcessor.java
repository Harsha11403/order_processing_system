package com.precize.project.order_processing_system.process;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precize.project.order_processing_system.domain.Order;
import com.precize.project.order_processing_system.domain.OrderItem;
import com.precize.project.order_processing_system.domain.OrderStatus;
import com.precize.project.order_processing_system.events.Event;
import com.precize.project.order_processing_system.events.OrderCancelledEvent;
import com.precize.project.order_processing_system.events.OrderCreatedEvent;
import com.precize.project.order_processing_system.events.PaymentReceivedEvent;
import com.precize.project.order_processing_system.events.ShippingScheduledEvent;
import com.precize.project.order_processing_system.observe.OrderObserver;
import com.precize.project.order_processing_system.repo.OrderRepository;

public class EventProcessor {

	private final OrderRepository repository;
	private final ObjectMapper mapper;
	private final List<OrderObserver> observers = new ArrayList<>();
	
	public EventProcessor(OrderRepository orderRepository, ObjectMapper mapper)
	{
		this.repository = orderRepository;
		this.mapper = mapper;
	}
	
	public void registerObserver(OrderObserver observer)
	{
		observers.add(observer);
	}
	
	public void processEvent(Event event)
	{
		if(event instanceof OrderCreatedEvent)
		{
			handleOrderCreated((OrderCreatedEvent) event);
		}
		else if(event instanceof OrderCancelledEvent)
		{
			handleCancel((OrderCancelledEvent) event);
		}
		else if(event instanceof ShippingScheduledEvent)
		{
			handleShipping((ShippingScheduledEvent) event);
		}
		else if(event instanceof PaymentReceivedEvent)
		{
			handlePayment((PaymentReceivedEvent) event);
		}
		else
		{
			System.err.println("WARN: Unknown/unsupported event type: " + event.getEventType());
		}
	}
	
	private void notifyObservers(Event event, Order before, Order after)
	{
		for(OrderObserver o : observers)
		{
			try
			{
				o.onEvent(event, before, after);
			}
			catch(Exception e)
			{
				System.err.println("Observer exception: " + e.getMessage());
			}
		}
	}
	
    private void handleOrderCreated(OrderCreatedEvent event) {
        String id = event.getOrderId();
        Optional<Order> existing = repository.findById(id);
        if (existing.isPresent()) {
            System.err.println("WARN: OrderCreated received but order already exists: " + id);
            return;
        }
        List<OrderItem> items = event.getItems() != null ? event.getItems() : List.of();
        Order order = new Order(id, event.getCustomerId(), items, event.getTotalAmount());
        order.appendToHistory(event);
        repository.save(order);
        notifyObservers(event, null, order);
    }

    private void handlePayment(PaymentReceivedEvent event) {
        String id = event.getOrderId();
        Optional<Order> opt = repository.findById(id);
        if (opt.isEmpty()) {
            System.err.println("WARN: PaymentReceived for unknown order: " + id);
            return;
        }
        Order before = copySnapshot(opt.get());
        Order order = opt.get();

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.SHIPPED) {
            System.err.println("WARN: PaymentReceived ignored. Order in terminal state: " + order.getStatus());
            return;
        }

        BigDecimal amount = event.getAmountPaid() == null ? BigDecimal.ZERO : event.getAmountPaid();
        order.addAmountPaid(amount);

        int cmp = order.getAmountPaid().compareTo(order.getTotalAmount());
        if (cmp < 0) {
            order.setStatus(OrderStatus.PARTIALLY_PAID);
        } else if (cmp == 0) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.PAID);
            System.err.println("WARN: Overpayment detected for order " + id + ". paid=" + order.getAmountPaid() + ", total=" + order.getTotalAmount());
        }

        order.appendToHistory(event);
        repository.save(order);
        notifyObservers(event, before, order);
    }

    private void handleShipping(ShippingScheduledEvent event) {
        String id = event.getOrderId();
        Optional<Order> opt = repository.findById(id);
        if (opt.isEmpty()) {
            System.err.println("WARN: ShippingScheduled for unknown order: " + id);
            return;
        }
        Order before = copySnapshot(opt.get());
        Order order = opt.get();

        if (order.getStatus() != OrderStatus.PAID) {
            System.err.println("WARN: ShippingScheduled ignored. Order not PAID. Current status: " + order.getStatus());
            return;
        }

        order.setShippingDate(event.getShippingDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        order.setStatus(OrderStatus.SHIPPED);
        order.appendToHistory(event);
        repository.save(order);
        notifyObservers(event, before, order);
    }

    private void handleCancel(OrderCancelledEvent event) {
        String id = event.getOrderId();
        Optional<Order> opt = repository.findById(id);
        if (opt.isEmpty()) {
            System.err.println("WARN: OrderCancelled for unknown order: " + id);
            return;
        }
        Order before = copySnapshot(opt.get());
        Order order = opt.get();

        if (order.getStatus() == OrderStatus.SHIPPED) {
            System.err.println("WARN: OrderCancelled ignored. Order already SHIPPED: " + id);
            return;
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.appendToHistory(event);
        repository.save(order);
        notifyObservers(event, before, order);
    }

    private Order copySnapshot(Order o) {
        
        Order copy = new Order(o.getOrderId(), o.getCustomerId(), o.getItems(), o.getTotalAmount());
        copy.setStatus(o.getStatus());
        copy.addAmountPaid(o.getAmountPaid());
        copy.setShippingDate(o.getShippingDate());
        return copy;
    }	
}
