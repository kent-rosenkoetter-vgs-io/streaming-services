package com.verygood.security.example.processor.file;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecordParser implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(RecordParser.class);

  private static final Pattern FIELD_PATTERN = Pattern.compile("(?:[^\\n\\r\\f,\\\\]|\\\\.)*");
  /**
   * The pattern for a record on each line.
   * <p>
   * {@code ^(?<Address>fieldPattern),(?<City>fieldPattern),(?<State>fieldPattern),(?<Zip>fieldPattern),(?<FirstName>fieldPattern),(?<LastName>fieldPattern),(?<CardType>fieldPattern),(?<CardNum>fieldPattern),(?<Expiration>fieldPattern),(?<CVV>fieldPattern)$}
   */
  private static final Pattern RECORD_PATTERN = Pattern
      .compile(
          "^(?<Address>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<City>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<State>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<Zip>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<FirstName>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<LastName>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<CardType>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<CardNum>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<Expiration>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<CVV>(?:[^\\n\\r\\f,\\\\]|\\\\.)*)$");

  private final BufferedReader bufferedReader;
  private final BlockingQueue<Record> recordsToRedact;

  public RecordParser(@NotNull final BufferedReader bufferedReader,
      @NotNull final BlockingQueue<Record> recordsToRedact) {
    this.bufferedReader = bufferedReader;
    this.recordsToRedact = recordsToRedact;
  }

  public Record parseRecord(final String line) {
    LOGGER.debug("Parsing record from '{}'", line);
    final Matcher matcher = RECORD_PATTERN.matcher(line);
    NewRelic.incrementCounter("linesParsed");
    if (matcher.matches()) {
      final String address = matcher.group("Address");
      final String city = matcher.group("City");
      final String state = matcher.group("State");
      final String zip = matcher.group("Zip");
      final String firstName = matcher.group("FirstName");
      final String lastName = matcher.group("LastName");
      final String cardType = matcher.group("CardType");
      final String cardNum = matcher.group("CardNum");
      final String expiration = matcher.group("Expiration");
      final String cvv = matcher.group("CVV");

      final Record record = new Record(address, city, state, zip, firstName, lastName, cardType, cardNum,
          expiration, cvv);

      NewRelic.incrementCounter("recordsParsed");

      return record;
    } else {
      throw new IllegalArgumentException(line);
    }
  }

  @Override
  @Trace(async = true)
  public void run() {
    LOGGER.info("Entering run()");
    try {
      String line = bufferedReader.readLine();
      while (line != null) {
        final Record record = parseRecord(line);
        LOGGER.debug("Queueing {}", record);
        recordsToRedact.put(record);
        line = bufferedReader.readLine();
      }
      recordsToRedact.put(Record.COMPLETED);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    LOGGER.info("Exiting run()");
  }
}
