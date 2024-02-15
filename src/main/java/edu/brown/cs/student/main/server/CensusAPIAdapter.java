package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.DataSource.DataSourceException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class connects server application to United States Census API. It will make outgoing
 * requests to the Census API based on incoming server requests, adapting Census data format to
 * expected server response format.
 */
public class CensusAPIAdapter {
    private void getStateCodes() throws DataSourceException {
        try {
            URL requestURL = new URL("https", "api.census.gov", "/data/2010/dec/sf1?get=NAME&for=state:*");
            HttpURLConnection clientConnection = connect(requestURL);
            Moshi moshi = new Moshi.Builder().build();

            // idk what type goes here quite yet...
            // JsonAdapter<> adapter = moshi.adapter().nonNull();
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
