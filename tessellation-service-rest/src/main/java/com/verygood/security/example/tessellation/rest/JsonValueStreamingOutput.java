package com.verygood.security.example.tessellation.rest;

import com.verygood.security.example.tessellation.logic.Tessellator;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class JsonValueStreamingOutput implements StreamingOutput {

  private final Tessellator tessellator;

  private final Stream<JsonValue> jsonValueStream;

  public JsonValueStreamingOutput(@NotNull final Tessellator tessellator,
      @NotNull final Stream<JsonValue> jsonValueStream) {
    this.tessellator = tessellator;
    this.jsonValueStream = jsonValueStream;
  }

  @Override
  public void write(final OutputStream output) throws IOException, WebApplicationException {
    try (final JsonGenerator jsonGenerator = Json.createGenerator(output)) {
      jsonGenerator.writeStartArray();
      jsonValueStream.forEach(jsonGenerator::write);
      jsonGenerator.writeEnd();
    }
  }
}
