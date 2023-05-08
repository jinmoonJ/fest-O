package com.example.festo.order.application.service;

import com.example.festo.order.adapter.in.web.model.OrderProduct;
import com.example.festo.order.adapter.in.web.model.OrderRequest;
import com.example.festo.order.adapter.in.web.model.OrderStatusChangeRequest;
import com.example.festo.order.adapter.out.persistence.ProductRepository;
import com.example.festo.order.application.port.in.OrderStatusChangeUseCase;
import com.example.festo.order.application.port.in.PlaceOrderUseCase;
import com.example.festo.order.application.port.out.LoadOrderPort;
import com.example.festo.order.application.port.out.PlaceOrderPort;
import com.example.festo.order.application.port.out.UpdateOrderPort;
import com.example.festo.order.domain.Order;
import com.example.festo.order.domain.OrderLine;
import com.example.festo.order.domain.OrderNo;
import com.example.festo.order.domain.Orderer;
import com.example.festo.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService implements PlaceOrderUseCase, OrderStatusChangeUseCase {

    private final PlaceOrderPort placeOrderPort;

    private final LoadOrderPort loadOrderPort;

    private final UpdateOrderPort updateOrderPort;

    private final ProductRepository productRepository;

    private final OrdererService ordererService;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLine> orderLines = new ArrayList<>();
        for (OrderProduct orderProduct : orderRequest.getOrderProducts()) {
            Product product = productRepository.findById(orderProduct.getProductId())
                                               .orElseThrow(NoSuchElementException::new);
            orderLines.add(new OrderLine(orderProduct.getProductId(), product.getPrice(), orderProduct.getQuantity()));
        }

        Orderer orderer = ordererService.createOrderer(orderRequest.getOrdererMemberId());
        OrderNo orderNo = OrderNo.of(placeOrderPort.nextOrderNo(orderRequest.getBoothId()));
        Order order = new Order(orderNo, orderer, orderLines);

        placeOrderPort.placeOrder(order);
    }

    @Transactional
    @Override
    public void changeStatus(Long orderId, OrderStatusChangeRequest orderStatusChangeRequest) {
        Order order = loadOrderPort.loadOrder(orderId);
        order.updateStatus(orderStatusChangeRequest);

        updateOrderPort.updateOrder(order);
    }
}