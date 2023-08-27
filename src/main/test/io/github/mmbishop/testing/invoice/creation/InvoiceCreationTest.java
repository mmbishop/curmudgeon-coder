package io.github.mmbishop.testing.invoice.creation;

import io.github.mmbishop.testing.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class InvoiceCreationTest {

    @Test
    void invoice_is_created_and_emailed_to_the_customer() {
        given_a_customer();
        and_an_order();
        and_an_order_processor();
        when_processing_the_order();
        then_an_invoice_is_created();
        and_the_invoice_is_emailed_to_the_customer();
    }

    private void given_a_customer() {
        customer = new Customer(UUID.randomUUID(), "John Smith");
    }

    private void and_an_order() {
        var items = List.of(
                new OrderItem(new Product(UUID.randomUUID(), "Big widget", 5.99), 1),
                new OrderItem(new Product(UUID.randomUUID(), "Little widget", 3.99), 2)
        );
        order = new Order(UUID.randomUUID(), customer, items);
    }

    private void and_an_order_processor() {
        invoiceEmailer = Mockito.mock(InvoiceEmailer.class);
        orderProcessor = new OrderProcessor(invoiceEmailer);
    }

    private void when_processing_the_order() {
        invoice = orderProcessor.process(order);
    }

    private void then_an_invoice_is_created() {
        assertNotNull(invoice);
        var order = invoice.getOrder();
        assertThat(order.getId(), is(order.getId()));
        assertThat(order.getCustomer(), is(customer));
        assertThat(order.getItems().size(), is(2));
    }

    private void and_the_invoice_is_emailed_to_the_customer() {
        verify(invoiceEmailer).email(invoice);
    }

    private Customer customer;
    private Invoice invoice;
    private InvoiceEmailer invoiceEmailer;
    private Order order;
    private OrderProcessor orderProcessor;
}
