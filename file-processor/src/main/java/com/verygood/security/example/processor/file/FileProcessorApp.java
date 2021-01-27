package com.verygood.security.example.processor.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessorApp {

  public static void main(final String[] args) throws IOException {
//    final String fieldRegex = "(?:[^\\n\\r\\f,\\\\]|\\\\.)*";
//    final Pattern fieldPattern = Pattern.compile(fieldRegex);
    // (?:)
    // (?<name>)
    // (?:[^\n\r\f,\\]|\\.)*
    // \\.
    // ^(?<Address>(?:[^\n\r\f,\\]|\\.)*),(?<City>(?:[^\n\r\f,\\]|\\.)*),(?<State>(?:[^\n\r\f,\\]|\\.)*),(?<Zip>(?:[^\n\r\f,\\]|\\.)*),(?<FirstName>(?:[^\n\r\f,\\]|\\.)*),(?<LastName>(?:[^\n\r\f,\\]|\\.)*),(?<CardType>(?:[^\n\r\f,\\]|\\.)*),(?<CardNum>(?:[^\n\r\f,\\]|\\.)*),(?<Expiration>(?:[^\n\r\f,\\]|\\.)*),(?<CVV>(?:[^\n\r\f,\\]|\\.)*)$
    final Pattern pattern = Pattern
        .compile("^(?<Address>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<City>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<State>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<Zip>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<FirstName>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<LastName>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<CardType>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<CardNum>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<Expiration>(?:[^\\n\\r\\f,\\\\]|\\\\.)*),(?<CVV>(?:[^\\n\\r\\f,\\\\]|\\\\.)*)$");
    try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(
            FileProcessorApp.class.getResourceAsStream("/SampleCSV.csv"),
            StandardCharsets.UTF_8))) {
      String line = reader.readLine();
      while (line != null) {
        final Matcher matcher = pattern.matcher(line);
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

          System.out.println(record);
        } else {
          System.out.println("no match");
        }
        line = reader.readLine();
      }
      // done
    }
  }

}
