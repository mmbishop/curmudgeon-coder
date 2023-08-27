package io.github.mmbishop.testing;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class OrderProcessor {

    private final InvoiceEmailer emailer;

    public Invoice process(Order order) {
        var totalCost = order.getItems().stream()
                .map(i -> i.getProduct().getCost() * i.getQuantity())
                .reduce(0.0, Double::sum);
        var invoice = new Invoice(UUID.randomUUID(), order, totalCost);
        emailer.email(invoice);
        return invoice;
    }
}
