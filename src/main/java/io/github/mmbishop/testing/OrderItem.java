package io.github.mmbishop.testing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderItem {

    private Product product;
    private int quantity;
}
