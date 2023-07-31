package ru.neoflex.conveyor.credit_conveyor.util.deserializers;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateNonBlockingDeserializer extends NonBlockingDeserializer<LocalDate>{
    public DateNonBlockingDeserializer() {
        super(new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
