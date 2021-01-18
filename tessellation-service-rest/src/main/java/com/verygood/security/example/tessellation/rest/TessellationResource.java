package com.verygood.security.example.tessellation.rest;

import com.verygood.security.example.tessellation.logic.JsonValueToPolygonMapper;
import com.verygood.security.example.tessellation.logic.Tessellator;
import com.verygood.security.example.tessellation.logic.TriangleToJsonObjectMapper;
import com.verygood.security.example.tessellation.model.Polygon;
import com.verygood.security.example.tessellation.model.Triangle;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.stream.JsonParser;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("tessellation")
public class TessellationResource {

  private final Tessellator tessellator;
  private final Function<JsonValue, Polygon> jsonValueToPolygonMapper;
  private final Function<Triangle, JsonValue> triangleToJsonValueMapper;

  @Inject
  public TessellationResource(@NotNull final Jsonb jsonb,
      @NotNull final Tessellator tessellator) {
    this.tessellator = tessellator;
    this.jsonValueToPolygonMapper = new JsonValueToPolygonMapper(jsonb);
    this.triangleToJsonValueMapper = new TriangleToJsonObjectMapper(jsonb);
  }

  @POST
  @Consumes({MediaType.APPLICATION_JSON,})
  @Produces({MediaType.APPLICATION_JSON,})
  public Response tessellate(@Context final HttpServletRequest httpServletRequest) throws IOException {
    final JsonParser parser = Json.createParser(httpServletRequest.getInputStream());

    final Stream<Polygon> polygonStream = parser.getArrayStream().map(jsonValueToPolygonMapper);
    final Stream<Triangle> triangleStream = tessellator.tessellatePolygons(polygonStream);
    final Stream<JsonValue> jsonValueStream = triangleStream.map(triangleToJsonValueMapper);
    return Response
        .ok()
        .type(MediaType.APPLICATION_JSON_TYPE)
        .entity(new JsonValueStreamingOutput(tessellator, jsonValueStream))
        .build();
  }

}
