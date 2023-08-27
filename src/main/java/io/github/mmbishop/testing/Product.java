package io.github.mmbishop.testing;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Product {

    private UUID id;
    private String name;
    private double cost;
}
