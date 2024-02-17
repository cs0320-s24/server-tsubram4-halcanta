package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.APICensusDataSource;
import edu.brown.cs.student.main.server.CensusHandler;
import edu.brown.cs.student.main.server.RequestHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestHandlers {
  private final JsonAdapter<Map<String, Object>> adapter;

  public TestHandlers() {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    adapter = moshi.adapter(type);
  }

  @BeforeAll
  public static void initialize() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  @BeforeEach
  public void setup() {
    APICensusDataSource source = new APICensusDataSource();
    CensusHandler handler = new CensusHandler(source);
    RequestHandler rh = new RequestHandler();
    Spark.get("csv", rh);
    Spark.get("broadband", handler);
    Spark.init();
    Spark.awaitInitialization();
  }

  @AfterEach
  public void teardown() {
    Spark.unmap("csv");
    Spark.unmap("broadband");
    Spark.awaitStop();
  }

  private static HttpURLConnection websiteRequest(String dest) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + dest);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testLoadAndViewCSV() throws IOException {
    HttpURLConnection connection1 =
        websiteRequest(
            "csv?csvCommand=loadCSV&filePath=data/census/income_by_race.csv&hasHeader=true");
    assertEquals(200, connection1.getResponseCode());

    HttpURLConnection connection2 =
        websiteRequest(
            "csv?csvCommand=viewCSV&filePath=data/census/income_by_race.csv&hasHeader=true");
    assertEquals(200, connection2.getResponseCode());

    Map<String, Object> responseMap =
        adapter.fromJson(new Buffer().readFrom(connection2.getInputStream()));
    assertEquals("success", responseMap.get("type"));
  }
}
