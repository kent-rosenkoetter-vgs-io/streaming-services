package com.verygood.security.example.processor.file;

import com.verygood.security.example.knox.grpc.AliasRequest;
import com.verygood.security.example.knox.grpc.KnoxServiceGrpc;
import com.verygood.security.example.knox.grpc.SensitiveDataAlias;
import io.grpc.stub.StreamObserver;

public class InlinePretendKnoxService extends KnoxServiceGrpc.KnoxServiceImplBase {

  @Override
  public void getOrCreateAlias(final AliasRequest request,
      final StreamObserver<SensitiveDataAlias> responseObserver) {
    responseObserver.onNext(SensitiveDataAlias.newBuilder()
        .setAlias("sensitive stuff")
        .build());
    responseObserver.onCompleted();
  }
}
