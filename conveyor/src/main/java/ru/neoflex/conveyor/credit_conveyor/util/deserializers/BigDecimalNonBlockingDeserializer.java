package ru.neoflex.conveyor.credit_conveyor.util.deserializers;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

import java.math.BigDecimal;

public class BigDecimalNonBlockingDeserializer extends NonBlockingDeserializer<BigDecimal>{
    public BigDecimalNonBlockingDeserializer() {
        super(new NumberDeserializers.BigDecimalDeserializer());
    }
}
