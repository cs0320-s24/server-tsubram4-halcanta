package edu.brown.cs.student.main.CSV;

import edu.brown.cs.student.main.Creator.CreatorFromRow;
import edu.brown.cs.student.main.Creator.FactoryFailureException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parse class parses a CSV data file
 *
 * @param <T> - Parse is of type T
 */
public class Parse<T> {

  static final Pattern regexSplitCSVRow = Pattern.compile(
      ",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  private BufferedReader reader;
  private boolean hasHeaders;
  private List<String> headers;
  private CreatorFromRow<T> creator;
  private String filepath;

  /**
   * @param reader     - Object which contains data
   * @param hasHeaders - boolean indicating if data has headers
   * @param creator    - Creator object which specifies what object each row is converted to
   * @param filePath   - String which represents data's filepath
   * @throws IOException - exception if there are any errors in input
   */
  public Parse(Object reader, boolean hasHeaders, CreatorFromRow<T> creator, String filePath)
      throws IOException {
    this.hasHeaders = hasHeaders;
    this.headers = new ArrayList<>();
    this.creator = creator;
    this.filepath = filePath;

    if (isPathAllowed(filePath) || filepath.equals("no")) {
      if (reader instanceof Reader) {
        this.reader = new BufferedReader((Reader) reader);
      } else if (reader instanceof String) {
        this.reader = new BufferedReader(new FileReader((String) reader));
      } else {
        throw new IllegalArgumentException(
            "Unsupported file type. CSV must be a Reader Object or a Filename String");
      }

      if (this.hasHeaders) {
        this.parseHeaders();
      }
    } else {
      throw new IllegalArgumentException("Access to this file is not allowed.");
    }
  }

  /**
   * @throws IOException - exception if headers are parsed incorrectly
   */
  private void parseHeaders() throws IOException {
    String line = this.reader.readLine();
    if (line != null) {
      this.headers.addAll(Arrays.asList(line.split(",")));
    }
  }

  /**
   * @return - List<String> representing headers in data
   */
  public List<String> getHeaders() {
    return this.headers;
  }

  /**
   * @return - List of elements of type T representing each
   * @throws IOException - Exception if there is a factory failure from the Creator object
   */
  public List<T> parse() throws IOException {
    List<T> records = new ArrayList<>();
    String line;
    int expectedColumnCount = hasHeaders ? headers.size() : -1;

    while ((line = this.reader.readLine()) != null) {
      // Split line using regex and apply postprocessing to each field
      String[] splitLine = regexSplitCSVRow.split(line);
      List<String> processedRecord = new ArrayList<>();

      for (String field : splitLine) {
        processedRecord.add(postprocess(field));
      }

      // Check for inconsistent column count
      if (expectedColumnCount != -1 && processedRecord.size() != expectedColumnCount) {
        throw new IOException("Inconsistent column count in CSV file");
      }

      try {
        records.add(this.creator.create(processedRecord));
      } catch (FactoryFailureException e) {
        System.err.println("Factory failure for row: " + e.row);
      }
    }
    return records;
  }

  /**
   * @param filePath - String of data's filePath
   * @return - boolean indicating if data is accessible via filepath
   */
  private static boolean isPathAllowed(String filePath) {
    String allowed_directory =
        "C:\\Users\\HP\\Desktop\\Tanay Subramanian\\Education\\Brown\\Sophomore\\CS 320\\csv-tanaysubramanian\\data";
    Path path = Paths.get(filePath).toAbsolutePath();
    return path.startsWith(allowed_directory);
  }

  /**
   * Eliminate a single instance of leading or trailing double-quote, and replace pairs of double
   * quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   */
  public static String postprocess(String arg) {
    return arg
        // Remove extra spaces at beginning and end of the line
        .trim()
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"");
  }
}
