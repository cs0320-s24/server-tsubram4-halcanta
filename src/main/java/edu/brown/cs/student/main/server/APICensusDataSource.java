package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.DataSource.DataSourceException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import okio.Buffer;

/**
 * This class connects server application to United States Census API. It will make outgoing
 * requests to the Census API based on incoming server requests, adapting Census data format to
 * expected server response format.
 */
public class APICensusDataSource implements CensusDataSource {
  List<List<String>> stateCodes;
  List<List<String>> countyCodes;

  List<List<String>> broadbandCensusInfo;

  namebroadBandStateCounty targetInfo;

  private void getStateCodes() throws DataSourceException {
    try {
      URL requestURL =
          new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
      System.out.println("set up getStateCodes URL");
      HttpURLConnection clientConnection = connect(requestURL);
      System.out.println("connected to getStateCodes URL");
      Moshi moshi = new Moshi.Builder().build();
      System.out.println("moshi set up");

      // may have to change this to reflect the grid typing from the livecode??
      // turning the data type an 2D array of strings
      Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
      System.out.println("mapStringObject type created");
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);
      System.out.println("JsonAdapter created");

      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      System.out.println("body translated from Json");
      clientConnection.disconnect();
      System.out.println("disconnected from client");

      if (body == null) {
        // come back to this later
        throw new DataSourceException();
      }

      System.out.println(body);
      this.stateCodes = body;
    } catch (IOException e) {
      throw new DataSourceException();
    }
  }

  private static HttpURLConnection connect(URL requestURL) throws IOException, DataSourceException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection)) {
      // add additional stuff to exception class later
      throw new DataSourceException();
    }
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect();
    if (clientConnection.getResponseCode() != 200) {
      // once again, add stuff to exception later
      throw new DataSourceException();
    }
    return clientConnection;
  }

  private void getCountyCodes(String state, String county) throws DataSourceException, IOException {
    System.out.println("Now acquiring Census data for " + county + ", " + state + ".");

    getStateCodes();

    String stateCode = null;

    for (List<String> item : this.stateCodes) {
      if (item.get(0).equalsIgnoreCase(state)) {
        stateCode = item.get(1);
      }
    }

    System.out.println("Found state code");
    System.out.println(stateCode);

    URL requestURL =
        new URL(
            "https",
            "api.census.gov",
            "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode);
    System.out.println("establishing URL");
    HttpURLConnection clientConnection = connect(requestURL);
    System.out.println("connecting to client");
    Moshi moshi = new Moshi.Builder().build();
    System.out.println("creating moshi");

    Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

    try {
      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      System.out.println("retrieving body info for county codes");
      clientConnection.disconnect();
      System.out.println("disconnecting from client");

      if (body == null) {
        // come back to this
        throw new DataSourceException();
      }

      System.out.println(body);
      this.countyCodes = body;
      // come back to this
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public BroadbandData getBroadbandData(String state, String county)
      throws DataSourceException, IOException {
    getCountyCodes(state, county);
    String countyCode = null;
    String stateCode = null;

    for (List<String> item : this.countyCodes) {
      if (item.get(0).equalsIgnoreCase(county + ", " + state)) {
        countyCode = item.get(2);
        stateCode = item.get(1);
      }
    }

    System.out.println("Found county code");
    System.out.println(countyCode);

    try {
      URL requestURL =
          new URL(
              "https",
              "api.census.gov",
              "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                  + countyCode
                  + "&in=state:"
                  + stateCode);
      HttpURLConnection clientConnection = connect(requestURL);
      Moshi moshi = new Moshi.Builder().build();

      Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
      System.out.println("mapStringObject type created");
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);
      System.out.println("JsonAdapter created");
      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();

      System.out.println(body);
      this.broadbandCensusInfo = body;

      String bb = body.get(1).get(1);
      Date time = new Date();
      this.targetInfo = new namebroadBandStateCounty(bb, time.toString());

    } catch (IOException e) {
      System.out.println("Come back to this");
    }

    // this should be returning a BroadbandData
    return new BroadbandData(
        county + ", " + state, this.targetInfo.S2802_C03_022E, targetInfo.date());
  }

  public record namebroadBandStateCounty(String S2802_C03_022E, String date) {}
}
