package com.verygood.security.example.tessellation.grpc.service;

import com.newrelic.api.agent.Trace;
import com.verygood.security.example.tessellation.grpc.Polygon;
import com.verygood.security.example.tessellation.grpc.Triangle;
import com.verygood.security.example.tessellation.grpc.Vertex;
import com.verygood.security.example.tessellation.logic.Tessellator;
import io.grpc.stub.StreamObserver;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PolygonToTriangleTessellator implements StreamObserver<Polygon> {

  private static final Logger LOGGER = LoggerFactory.getLogger(PolygonToTriangleTessellator.class);

  private final Tessellator tessellator;

  private final StreamObserver<Triangle> responseObserver;

  @Inject
  public PolygonToTriangleTessellator(@NotNull final Tessellator tessellator,
      @NotNull final StreamObserver<Triangle> responseObserver) {
    this.tessellator = tessellator;
    this.responseObserver = responseObserver;
  }

  @Override
  @Trace
  public void onNext(final Polygon value) {
    for (final com.verygood.security.example.tessellation.model.Triangle triangle
        : tessellator.tessellatePolygon(map(value))) {
      responseObserver.onNext(map(triangle));
    }
  }

  @Override
  @Trace
  public void onError(final Throwable t) {
    LOGGER.error("foo", t);
  }

  @Override
  @Trace
  public void onCompleted() {
    responseObserver.onCompleted();
  }

  private Polygon map(final com.verygood.security.example.tessellation.model.Polygon polygon) {
    final Polygon.Builder builder = Polygon.newBuilder();
    polygon.getVertices().stream().map(this::map).forEach(builder::addVertex);
    return builder.build();
  }

  private com.verygood.security.example.tessellation.model.Polygon map(final Polygon polygon) {
    return new com.verygood.security.example.tessellation.model.Polygon(polygon.getVertexList().stream()
        .map(this::map)
        .collect(Collectors.toList()));
  }

  private Triangle map(final com.verygood.security.example.tessellation.model.Triangle triangle) {
    final Triangle.Builder builder = Triangle.newBuilder();
    builder.setVertex1(map(triangle.getVertices().get(0)));
    builder.setVertex2(map(triangle.getVertices().get(1)));
    builder.setVertex3(map(triangle.getVertices().get(2)));
    return builder.build();
  }

  private com.verygood.security.example.tessellation.model.Triangle map(final Triangle triangle) {
    return new com.verygood.security.example.tessellation.model.Triangle(Arrays.asList(
        map(triangle.getVertex1()),
        map(triangle.getVertex2()),
        map(triangle.getVertex3())));
  }

  private Vertex map(final com.verygood.security.example.tessellation.model.Vertex vertex) {
    final Vertex.Builder vertexBuilder = Vertex.newBuilder();
    vertexBuilder.setX(vertex.getX());
    vertexBuilder.setY(vertex.getY());
    vertexBuilder.setZ(vertex.getZ());
    return vertexBuilder.build();
  }

  private com.verygood.security.example.tessellation.model.Vertex map(final Vertex vertex) {
    return new com.verygood.security.example.tessellation.model.Vertex(vertex.getX(), vertex.getY(), vertex.getZ());
  }

}
