package ru.neoflex.conveyor.credit_conveyor.util.deserializers;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

public class IntegerNonBlockingDeserializer extends NonBlockingDeserializer<Integer>{
    public IntegerNonBlockingDeserializer() {
        super(new NumberDeserializers.IntegerDeserializer(Integer.class,null));
    }
}

