package com.verygood.security.example.tessellation.rest;

import java.util.function.Consumer;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.validation.constraints.NotNull;

/**
 * A consumer that writes any input value to a streaming {@link JsonGenerator}.
 */
public class WriteAsJsonAction implements Consumer<JsonValue> {

  private final JsonGenerator jsonGenerator;

  public WriteAsJsonAction(@NotNull final JsonGenerator jsonGenerator) {
    this.jsonGenerator = jsonGenerator;
  }

  @Override
  public void accept(final JsonValue jsonValue) {
    jsonGenerator.write(jsonValue);
  }
}
