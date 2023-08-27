package io.github.mmbishop.testing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Invoice {

    private UUID id;
    private Order order;
    private double totalCost;
}
