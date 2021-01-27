package com.verygood.security.example.processor.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileDuplicator {

  public static final int COPIES = 100_000;

  public static void main(final String[] args) throws IOException {
    try (final BufferedWriter bufferedWriter = new BufferedWriter(
        new OutputStreamWriter(
            new FileOutputStream("huge.csv"),
            StandardCharsets.UTF_8))) {
      for (int i = 0; i < COPIES; i++) {
        try (final BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(
                FileProcessorApp.class.getResourceAsStream("/SampleCSV.csv"),
                StandardCharsets.UTF_8))) {
          String line = bufferedReader.readLine();
          while (line != null) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            line = bufferedReader.readLine();
          }
        }
      }
    }
  }

}
