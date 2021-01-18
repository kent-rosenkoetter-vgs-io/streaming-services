package com.verygood.security.example.tessellation.rest;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.verygood.security.example.tessellation.logic.Tessellator;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonValueStreamingOutput implements StreamingOutput {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonValueStreamingOutput.class);

  private final Tessellator tessellator;

  private final Stream<JsonValue> jsonValueStream;

  @Inject
  public JsonValueStreamingOutput(@NotNull final Tessellator tessellator,
      @NotNull final Stream<JsonValue> jsonValueStream) {
    this.tessellator = tessellator;
    this.jsonValueStream = jsonValueStream;
  }

  @Override
  @Trace
  public void write(final OutputStream output) throws IOException, WebApplicationException {
    try (final JsonGenerator jsonGenerator = Json.createGenerator(output)) {
      jsonGenerator.writeStartArray();
      jsonValueStream.forEach(new WriteAsJsonAction(jsonGenerator).andThen(jsonValue -> NewRelic.incrementCounter("foo")));
      jsonGenerator.writeEnd();
    }
  }

}
