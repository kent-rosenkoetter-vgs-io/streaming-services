package com.verygood.security.example.processor.file;

import com.verygood.security.example.knox.grpc.AliasRequest;
import com.verygood.security.example.knox.grpc.KnoxServiceGrpc;
import com.verygood.security.example.knox.grpc.SensitiveDataAlias;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProcessorApp {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessorApp.class);

  public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {
    final String serverName = InProcessServerBuilder.generateName();
    final Server server = InProcessServerBuilder.forName(serverName)
        .addService(new KnoxServiceGrpc.KnoxServiceImplBase() {
          @Override
          public void getOrCreateAlias(final AliasRequest request,
              final StreamObserver<SensitiveDataAlias> responseObserver) {
            responseObserver.onNext(SensitiveDataAlias.newBuilder()
                .setAlias("sensitive stuff")
                .build());
            responseObserver.onCompleted();
          }
        })
        .build().start();
    final ManagedChannel channel = InProcessChannelBuilder.forName(serverName).build();
    final KnoxServiceGrpc.KnoxServiceFutureStub knoxServiceFutureStub = KnoxServiceGrpc.newFutureStub(channel);

    final BlockingQueue<Record> recordsToRedact = new ArrayBlockingQueue<>(64);
    final BlockingQueue<AliasTask> pendingRedactions = new ArrayBlockingQueue<>(64);
    final BlockingQueue<Record> redactedRecords = new ArrayBlockingQueue<>(64);

    final ExecutorService executorService = Executors.newCachedThreadPool();

    final long startTime = System.currentTimeMillis();
    try (final BufferedWriter bufferedWriter = new BufferedWriter(
        new OutputStreamWriter(
            new FileOutputStream("output.csv"),
            StandardCharsets.UTF_8))) {
      final Future<?> writerFuture = executorService.submit(new RecordWriter(redactedRecords, bufferedWriter));
      executorService.submit(new Redactor(pendingRedactions, redactedRecords));
      executorService.submit(new AliasRequester(knoxServiceFutureStub, recordsToRedact, pendingRedactions));

      // FileProcessorApp.class.getResourceAsStream("/SampleCSV.csv")
      try (final BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(
              new FileInputStream("huge.csv"),
              StandardCharsets.UTF_8))) {
        final RecordParser task = new RecordParser(bufferedReader, recordsToRedact);
        task.run();
//        final Future<?> readerFuture = executorService.submit(task);
//        readerFuture.get();
      }

      // block until queues are drained
      writerFuture.get();
      bufferedWriter.flush();
    }
    final long endTime = System.currentTimeMillis();
    LOGGER.info("Total duration: {}", Duration.ofMillis(endTime - startTime));

    executorService.shutdown();
    server.shutdown();
  }

}
