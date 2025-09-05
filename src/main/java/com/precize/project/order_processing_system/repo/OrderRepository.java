package com.precize.project.order_processing_system.repo;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.precize.project.order_processing_system.domain.Order;

public class OrderRepository {

	private final Map<String, Order> store = new ConcurrentHashMap<>();
	
	public Optional<Order> findById(String id)
	{
		return Optional.ofNullable(store.get(id));
	}
	
	public void save(Order order)
	{
		store.put(order.getOrderId(), order);
	}
	
	public Collection<Order> findAll()
	{
		return store.values();
	}
}
