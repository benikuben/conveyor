package ru.neoflex.conveyor.credit_conveyor.util.deserializers;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

public class BooleanNonBlockingDeserializer extends NonBlockingDeserializer<Boolean> {
    public BooleanNonBlockingDeserializer() {
        super(new NumberDeserializers.BooleanDeserializer(Boolean.class, null));
    }
}
