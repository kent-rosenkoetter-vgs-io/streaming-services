package com.verygood.security.example.tessellation.logic;

import com.verygood.security.example.tessellation.model.Polygon;
import java.util.function.Function;
import javax.inject.Inject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.validation.constraints.NotNull;

public class JsonValueToPolygonMapper implements Function<JsonValue, Polygon> {

  private final Jsonb jsonb;

  @Inject
  public JsonValueToPolygonMapper(@NotNull final Jsonb jsonb) {
    this.jsonb = jsonb;
  }

  @Override
  public Polygon apply(final JsonValue jsonValue) {
    return jsonb.fromJson(jsonb.toJson(jsonValue), Polygon.class);
  }
}
