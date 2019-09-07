package edu.ignite.computing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PriceCategory {

    CHEAP(0,50),
    NORMAL(50,100),
    EXPENSIVE(100,10000),
    BUG(-1,-1);

    private final double bottom;
    private final double top;

    public static PriceCategory check(double price){
        return Arrays.stream(PriceCategory.values())
                .filter(category -> price >= category.bottom && price < category.top)
                .findFirst()
                .orElse(BUG);
    }
}
