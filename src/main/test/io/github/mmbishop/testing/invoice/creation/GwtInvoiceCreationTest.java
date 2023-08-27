package io.github.mmbishop.testing.invoice.creation;

import io.github.mmbishop.gwttest.core.GwtTest;
import io.github.mmbishop.gwttest.functions.GwtFunction;
import io.github.mmbishop.gwttest.model.Context;
import io.github.mmbishop.testing.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class GwtInvoiceCreationTest {

    @Test
    void invoice_is_created_and_emailed_to_the_customer() {
        gwt.test()
                .given(a_customer)
                .and(an_order)
                .and(an_order_processor)
                .when(processing_the_order)
                .then(an_invoice_is_created)
                .and(the_invoice_is_emailed_to_the_customer);
    }

    private final GwtFunction<InvoiceTestContext> a_customer
            = context -> context.customer = new Customer(UUID.randomUUID(), "John Smith");

    private final GwtFunction<InvoiceTestContext> an_order = context -> {
        var items = List.of(
                new OrderItem(new Product(UUID.randomUUID(), "Big widget", 5.99), 1),
                new OrderItem(new Product(UUID.randomUUID(), "Little widget", 3.99), 2)
        );
        context.order = new Order(UUID.randomUUID(), context.customer, items);
    };

    private final GwtFunction<InvoiceTestContext> an_order_processor = context -> {
        context.invoiceEmailer = Mockito.mock(InvoiceEmailer.class);
        context.orderProcessor = new OrderProcessor(context.invoiceEmailer);
    };

    private final GwtFunction<InvoiceTestContext> processing_the_order
            = context -> context.invoice = context.orderProcessor.process(context.order);

    private final GwtFunction<InvoiceTestContext> an_invoice_is_created = context -> {
        assertNotNull(context.invoice);
        var order = context.invoice.getOrder();
        assertThat(order.getId(), is(order.getId()));
        assertThat(order.getCustomer(), is(context.customer));
        assertThat(order.getItems().size(), is(2));
    };

    private final GwtFunction<InvoiceTestContext> the_invoice_is_emailed_to_the_customer
            = context -> verify(context.invoiceEmailer).email(context.invoice);

    private final GwtTest<InvoiceTestContext> gwt = new GwtTest<>(InvoiceTestContext.class);

    public static final class InvoiceTestContext extends Context {
        private Customer customer;
        private Invoice invoice;
        private InvoiceEmailer invoiceEmailer;
        private Order order;
        private OrderProcessor orderProcessor;
    }
}
