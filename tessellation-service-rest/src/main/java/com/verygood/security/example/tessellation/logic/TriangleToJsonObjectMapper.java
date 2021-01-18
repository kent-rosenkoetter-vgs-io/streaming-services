package com.verygood.security.example.tessellation.logic;

import com.verygood.security.example.tessellation.model.Triangle;
import java.io.StringReader;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.validation.constraints.NotNull;

public class TriangleToJsonObjectMapper implements Function<Triangle, JsonValue> {

  private final Jsonb jsonb;

  public TriangleToJsonObjectMapper(@NotNull final Jsonb jsonb) {
    this.jsonb = jsonb;
  }

  @Override
  public JsonValue apply(final Triangle polygon) {
    final JsonReader jsonReader = Json.createReader(new StringReader(jsonb.toJson(polygon)));
    return jsonReader.readValue();
  }
}
