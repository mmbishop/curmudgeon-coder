package io.github.mmbishop.testing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Order {

    private UUID id;
    private Customer customer;
    private List<OrderItem> items;
}
