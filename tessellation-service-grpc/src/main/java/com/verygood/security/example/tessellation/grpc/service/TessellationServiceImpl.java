package com.verygood.security.example.tessellation.grpc.service;

import com.verygood.security.example.tessellation.grpc.*;
import com.verygood.security.example.tessellation.logic.Tessellator;
import io.grpc.stub.StreamObserver;
import javax.validation.constraints.NotNull;

public class TessellationServiceImpl extends TessellationServiceGrpc.TessellationServiceImplBase {

  private final Tessellator tessellator;

  public TessellationServiceImpl(@NotNull final Tessellator tessellator) {
    this.tessellator = tessellator;
  }

  private Polygon map(final com.verygood.security.example.tessellation.model.Polygon polygon) {
    return null;
  }

  private com.verygood.security.example.tessellation.model.Polygon map(final Polygon polygon) {
    return null;
  }

  private Triangle map(final com.verygood.security.example.tessellation.model.Triangle vertex) {
    return null;
  }

  private com.verygood.security.example.tessellation.model.Triangle map(final Triangle vertex) {
    return null;
  }

  @Override
  public StreamObserver<Polygon> tessellate(final StreamObserver<Triangle> responseObserver) {
    return new StreamObserver<Polygon>() {
      @Override
      public void onNext(final Polygon value) {
        for (final com.verygood.security.example.tessellation.model.Triangle triangle : tessellator.tessellatePolygon(map(value))) {
          responseObserver.onNext(map(triangle));
        }
      }

      @Override
      public void onError(final Throwable t) {
        responseObserver.onError(t);
      }

      @Override
      public void onCompleted() {
        responseObserver.onCompleted();
      }
    };
  }
}
