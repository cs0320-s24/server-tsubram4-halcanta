package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVHandler.CSV.Parse;
import edu.brown.cs.student.main.CSVHandler.CSV.Search;
import edu.brown.cs.student.main.CSVHandler.Creator.Creator;
import edu.brown.cs.student.main.DataSource.DataSourceException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class handles incoming requests to the server and supports various endpoints. */
public class RequestHandler implements Route {

  private Object loadedCSV;

  /**
   * Handles requests to view the contents of the currently loaded CSV file. Returns the entire CSV
   * file's contents as a JSON 2-dimensional array.
   */
  private Object viewCSV(String filepath, String hasHeaders)
      throws IOException, DataSourceException {
    boolean headers = Boolean.parseBoolean(hasHeaders);
    Creator creator = new Creator();

    try {
      Parse parser = new Parse(filepath, headers, creator, filepath);
      List<List<String>> csvData = parser.parse();

      // Serialize csvData to JSON
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(type);

      return jsonAdapter.toJson(csvData);
    } catch (IOException | IllegalArgumentException e) {
      throw new DataSourceException("CSV file could not be accurately loaded");
    }
  }
  ;

  /**
   * Handles requests to search the contents of the currently loaded CSV file. Supports searching by
   * column index, column header, or across all columns.
   */
  public Object searchCSV(
      String filepath, String hasHeaders, String searchKey, String colIdentifier)
      throws DataSourceException, IOException {

    boolean headers = Boolean.parseBoolean(hasHeaders);
    Creator creator = new Creator();
    Parse parser = new Parse(filepath, headers, creator, filepath);
    Search search = new Search(parser, searchKey, colIdentifier);

    try {
      List<List<String>> searchResults = search.search();
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(type);

      return jsonAdapter.toJson(searchResults);

    } catch (IOException e) {
      throw new DataSourceException("Search algorithm encountered problems while searching...");
    }
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();

    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();

    try {

      String csvCommand = request.queryParams("csvCommand");
      String filePath = request.queryParams("filePath");
      String hasHeaders = request.queryParams("hasHeader");

      if (csvCommand == null || filePath == null) {
        responseMap.put("type", "error");
        responseMap.put("error_arg", csvCommand == null ? "state" : "county");
        return adapter.toJson(responseMap);
      }

      switch (csvCommand) {
        case "loadCSV" -> {
          if (this.loadedCSV == null) {
            responseMap.put("type", "success");
            responseMap.put("filepath", filePath);
            this.loadedCSV = this.viewCSV(filePath, hasHeaders);
          } else {
            responseMap.put("type", "success");
            responseMap.put("file", this.loadedCSV);
          }
          return adapter.toJson(responseMap);
        }
        case "viewCSV" -> {
          responseMap.put("type", "success");
          responseMap.put("csvContents", this.viewCSV(filePath, hasHeaders));
          return adapter.toJson(responseMap);
        }
        case "searchCSV" -> {
          String searchKey = request.queryParams("searchKey");
          String colIdentifier = request.queryParams("colIdentifier");
          responseMap.put("type", "success");
          responseMap.put(
              "matches", this.searchCSV(filePath, hasHeaders, searchKey, colIdentifier));
          return adapter.toJson(responseMap);
        }
      }
    } catch (DataSourceException e) {
      responseMap.put("type", "error");
      responseMap.put("error", "file could not be accessed");
      return adapter.toJson(responseMap);
    }
    return adapter.toJson(responseMap);
  }
}
