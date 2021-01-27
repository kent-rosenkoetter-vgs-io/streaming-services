package com.verygood.security.example.processor.file;

import com.verygood.security.example.knox.grpc.SensitiveDataAlias;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class AliasTask {

  public static final AliasTask COMPLETED = new AliasTask(Record.COMPLETED, new CompletableFuture<>()) {};

  private final Record record;
  private final Future<SensitiveDataAlias> future;

  public AliasTask(final Record record,
      final Future<SensitiveDataAlias> future) {
    this.record = record;
    this.future = future;
  }

  public Record getRecord() {
    return record;
  }

  public Future<SensitiveDataAlias> getFuture() {
    return future;
  }
}
