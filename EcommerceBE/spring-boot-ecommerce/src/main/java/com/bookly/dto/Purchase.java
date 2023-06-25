package com.bookly.dto;

import com.bookly.entity.Address;
import com.bookly.entity.Customer;
import com.bookly.entity.Order;
import com.bookly.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {
    private Customer customer;
    private Address shippingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
