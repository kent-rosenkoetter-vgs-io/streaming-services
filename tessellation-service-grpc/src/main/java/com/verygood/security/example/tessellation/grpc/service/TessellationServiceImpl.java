package com.verygood.security.example.tessellation.grpc.service;

import com.newrelic.api.agent.Trace;
import com.verygood.security.example.tessellation.grpc.Polygon;
import com.verygood.security.example.tessellation.grpc.TessellationServiceGrpc;
import com.verygood.security.example.tessellation.grpc.Triangle;
import com.verygood.security.example.tessellation.logic.Tessellator;
import io.grpc.stub.StreamObserver;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TessellationServiceImpl extends TessellationServiceGrpc.TessellationServiceImplBase {

  private static final Logger LOGGER = LoggerFactory.getLogger(TessellationServiceImpl.class);

  private final Tessellator tessellator;

  @Inject
  public TessellationServiceImpl(@NotNull final Tessellator tessellator) {
    this.tessellator = tessellator;
  }

  @Override
  @Trace
  public StreamObserver<Polygon> tessellate(final StreamObserver<Triangle> responseObserver) {
    return new PolygonToTriangleTessellator(tessellator, responseObserver);
  }

}
