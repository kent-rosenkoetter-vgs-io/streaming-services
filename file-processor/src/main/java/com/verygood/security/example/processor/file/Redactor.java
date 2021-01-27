package com.verygood.security.example.processor.file;

import com.newrelic.api.agent.Trace;
import com.verygood.security.example.knox.grpc.SensitiveDataAlias;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Redactor implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(Redactor.class);

  private final BlockingQueue<AliasTask> pendingRedactions;

  private final BlockingQueue<Record> redactedRecords;

  public Redactor(
      @NotNull final BlockingQueue<AliasTask> pendingRedactions,
      @NotNull final BlockingQueue<Record> redactedRecords) {
    this.pendingRedactions = pendingRedactions;
    this.redactedRecords = redactedRecords;
  }

  @Override
  @Trace(async = true)
  public void run() {
    LOGGER.info("Entering run()");
    try {
      AliasTask task = pendingRedactions.take();
      while (task != AliasTask.COMPLETED) {
        final SensitiveDataAlias sensitiveDataAlias = readKnoxResponse(task);
        final Record redactedRecord = createRedactedRecord(task.getRecord(), sensitiveDataAlias);
        redactedRecords.put(redactedRecord);
        task = pendingRedactions.take();
      }
      redactedRecords.put(Record.COMPLETED);
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    LOGGER.info("Exiting run()");
  }

  @Trace
  private SensitiveDataAlias readKnoxResponse(final AliasTask task)
      throws InterruptedException, ExecutionException {
    final Future<SensitiveDataAlias> future = task.getFuture();
    final SensitiveDataAlias sensitiveDataAlias = future.get();
    LOGGER.debug("Received {}", sensitiveDataAlias);
    return sensitiveDataAlias;
  }

  private Record createRedactedRecord(final Record record, final SensitiveDataAlias sensitiveDataAlias) {
    return new Record(record.getAddress(),
        record.getCity(),
        record.getState(),
        record.getZip(),
        record.getFirstName(),
        record.getLastName(),
        record.getCardType(),
        sensitiveDataAlias.getAlias(),
        record.getExpiration(),
        record.getCVV());
  }
}
