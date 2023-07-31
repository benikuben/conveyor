package ru.neoflex.conveyor.credit_conveyor.util.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.NoArgsConstructor;

import java.io.IOException;
@NoArgsConstructor
public abstract class NonBlockingDeserializer<T> extends JsonDeserializer<T> {
    private StdDeserializer<T> delegate;

    public NonBlockingDeserializer(StdDeserializer<T> _delegate) {
        this.delegate = _delegate;
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        try {
            return delegate.deserialize(jp, ctxt);
        } catch (JsonMappingException e) {
            return null;
        }
    }
}