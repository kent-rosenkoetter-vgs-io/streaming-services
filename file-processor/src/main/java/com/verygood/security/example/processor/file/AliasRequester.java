package com.verygood.security.example.processor.file;

import com.newrelic.api.agent.Trace;
import com.verygood.security.example.knox.grpc.AliasRequest;
import com.verygood.security.example.knox.grpc.KnoxServiceGrpc;
import com.verygood.security.example.knox.grpc.SensitiveDataAlias;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliasRequester implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AliasRequester.class);

  private final KnoxServiceGrpc.KnoxServiceFutureStub knoxServiceFutureStub;

  private final BlockingQueue<Record> recordsToRedact;

  private final BlockingQueue<AliasTask> pendingRedactions;

  public AliasRequester(
      @NotNull final KnoxServiceGrpc.KnoxServiceFutureStub knoxServiceFutureStub,
      @NotNull final BlockingQueue<Record> recordsToRedact,
      @NotNull final BlockingQueue<AliasTask> pendingRedactions) {
    this.knoxServiceFutureStub = knoxServiceFutureStub;
    this.recordsToRedact = recordsToRedact;
    this.pendingRedactions = pendingRedactions;
  }

  @Override
  @Trace(async = true)
  public void run() {
    LOGGER.info("Entering run()");
    try {
      Record record = recordsToRedact.take();
      while (record != Record.COMPLETED) {
        final Future<SensitiveDataAlias> future = requestAlias(record);
        final AliasTask aliasTask = new AliasTask(record, future);
        pendingRedactions.put(aliasTask);
        record = recordsToRedact.take();
      }
      pendingRedactions.put(AliasTask.COMPLETED);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    LOGGER.info("Exiting run()");
  }

  @Trace
  private Future<SensitiveDataAlias> requestAlias(final Record record) {
    LOGGER.debug("Requesting alias for {}", record);
    final AliasRequest aliasRequest = AliasRequest.newBuilder()
        .setSensitiveData(record.getCardNum())
        .setFormat("format")
        .build();
    return knoxServiceFutureStub.getOrCreateAlias(aliasRequest);
  }

}
