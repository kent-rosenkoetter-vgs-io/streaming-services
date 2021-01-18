package com.verygood.security.example.tessellation.client;

import com.verygood.security.example.tessellation.grpc.TessellationServiceGrpc;
import com.verygood.security.example.tessellation.model.Polygon;
import com.verygood.security.example.tessellation.model.Triangle;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;

public class TessellationClientImpl implements TessellationClient {

  private final TessellationServiceGrpc.TessellationServiceStub stub;

  public TessellationClientImpl(@NotNull final TessellationServiceGrpc.TessellationServiceStub stub) {
    this.stub = stub;
  }

  @Override
  @NotNull
  public Stream<@NotNull Triangle> tessellate(@NotNull final Stream<@NotNull Polygon> polygons) {
    return null;
  }

}
