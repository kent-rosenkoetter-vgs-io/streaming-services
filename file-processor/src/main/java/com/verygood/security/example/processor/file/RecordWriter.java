package com.verygood.security.example.processor.file;

import com.newrelic.api.agent.Trace;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordWriter implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecordWriter.class);

  private final BlockingQueue<Record> redactedRecords;

  private final BufferedWriter writer;

  public RecordWriter(
      @NotNull final BlockingQueue<Record> redactedRecords,
      @NotNull final BufferedWriter writer) {
    this.redactedRecords = redactedRecords;
    this.writer = writer;
  }

  @Override
  @Trace(async = true)
  public void run() {
    LOGGER.info("Entering run()");
    try {
      Record record = redactedRecords.take();
      while (record != Record.COMPLETED) {
        writeRecord(record);
        record = redactedRecords.take();
      }
      writer.flush();
    } catch (final InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    }
    LOGGER.info("Exiting run()");
  }

  @Trace
  private void writeRecord(final Record record) throws IOException {
    LOGGER.debug("Writing {}", record);
    /*
     * There is no need to escape the fields because we never un-escaped them when reading them.
     */
    writer.write(record.getAddress());
    writer.write(',');
    writer.write(record.getCity());
    writer.write(',');
    writer.write(record.getState());
    writer.write(',');
    writer.write(record.getZip());
    writer.write(',');
    writer.write(record.getFirstName());
    writer.write(',');
    writer.write(record.getLastName());
    writer.write(',');
    writer.write(record.getCardType());
    writer.write(',');
    writer.write(record.getCardNum());
    writer.write(',');
    writer.write(record.getExpiration());
    writer.write(',');
    writer.write(record.getCVV());
    writer.newLine();
  }
}
