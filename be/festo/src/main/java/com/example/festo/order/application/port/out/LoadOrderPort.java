package com.example.festo.order.application.port.out;

import com.example.festo.order.domain.Order;

import java.util.List;

public interface LoadOrderPort {

    Order loadOrder(Long orderId);

    List<Order> loadOrdersByOrdererId(Long ordererId);

    List<Order> loadOrdersByBoothId(Long boothId, Long requesterId, boolean completed);

    List<Order> loadOrdersByBoothId(Long boothId, boolean completed);
}
