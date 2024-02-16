package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVHandler.CSV.Parse;
import edu.brown.cs.student.main.CSVHandler.CSV.Search;
import edu.brown.cs.student.main.CSVHandler.Creator.Creator;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import spark.Route;

/** This class handles incoming requests to the server and supports various endpoints. */
public class RequestHandler {

  /**
   * Handles requests to load a CSV file. The request is expected to contain the file path of the
   * CSV to load.
   */
  public static Route loadCsv =
      (request, response) -> {
        String filepath = request.queryParams("filepath");
        boolean headers = "yes".equalsIgnoreCase(request.queryParams("headers"));
        Creator creator = new Creator();

        try {
          Parse csvParser = new Parse<>(filepath, headers, creator, filepath);
          response.status(200); // HTTP 200 OK
          return "{\"filepath\": \""
              + filepath
              + "\", \"message\": \"CSV file loaded successfully.\"}";
        } catch (IOException | IllegalArgumentException e) {
          e.printStackTrace();
          response.status(500); // HTTP 500 Internal Server Error
          return "{\"filepath\": \""
              + filepath
              + "\", \"message\": \"Failed to load CSV file: "
              + e.getMessage()
              + "\"}";
        }
      };

  /**
   * Handles requests to view the contents of the currently loaded CSV file. Returns the entire CSV
   * file's contents as a JSON 2-dimensional array.
   */
  public static Route viewCsv =
      (request, response) -> {
        String filepath = request.queryParams("filepath");
        boolean headers = "yes".equalsIgnoreCase(request.queryParams("headers"));
        Creator creator = new Creator();

        Parse parser = new Parse(filepath, headers, creator, filepath);
        List<List<String>> csvData = parser.parse();

        // Serialize csvData to JSON
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, List.class, String.class);
        JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(type);

        String json = jsonAdapter.toJson(csvData);
        response.type("application/json");
        return "{\"data\": " + json + "}";
      };

  /**
   * Handles requests to search the contents of the currently loaded CSV file. Supports searching by
   * column index, column header, or across all columns.
   */
  public static Route searchCsv =
      (request, response) -> {
        String filepath = request.queryParams("filepath");
        boolean headers = "yes".equalsIgnoreCase(request.queryParams("headers"));
        Creator creator = new Creator();
        String value = request.queryParams("value");
        String colIdentifier = request.queryParams("colIdentifier");
        Parse parser = new Parse(filepath, headers, creator, filepath);
        Search search = new Search(parser, value, colIdentifier);

        try {
          List<List<String>> searchResults = search.search();
          Moshi moshi = new Moshi.Builder().build();
          Type type = Types.newParameterizedType(List.class, List.class, String.class);
          JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(type);

          String json = jsonAdapter.toJson(searchResults);
          response.type("application/json");

          return String.format(
              "{\"data\": %s, \"searchParameters\": {\"value\": \"%s\", \"colIdentifier\": \"%s\"}}",
              json, value, colIdentifier);
        } catch (IOException e) {
          e.printStackTrace();
          response.status(500); // HTTP 500 Internal Server Error
          return "{\"message\": \"Error during search: " + e.getMessage() + "\"}";
        }
      };
}
