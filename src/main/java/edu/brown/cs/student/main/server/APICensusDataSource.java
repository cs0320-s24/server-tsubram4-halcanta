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
import java.util.List;
import okio.Buffer;

/**
 * This class connects server application to United States Census API. It will make outgoing
 * requests to the Census API based on incoming server requests, adapting Census data format to
 * expected server response format.
 */
public class APICensusDataSource implements CensusDataSource {

  private static List<List<String>> getStateCodes() throws DataSourceException {
    try {
      URL requestURL =
          new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
      HttpURLConnection clientConnection = connect(requestURL);
      Moshi moshi = new Moshi.Builder().build();

      // may have to change this to reflect the grid typing from the livecode??
      Type listListString = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listListString);

      List<List<String>> body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();

      if (body == null) {
        // come back to this later
        throw new DataSourceException();
      }

      return body;
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
}
