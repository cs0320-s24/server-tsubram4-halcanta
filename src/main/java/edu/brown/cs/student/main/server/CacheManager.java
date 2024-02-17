package edu.brown.cs.student.main.server;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.DataSource.DataSourceException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okio.Buffer;

/**
 * This class caches information retrieved from the ACS to avoid sending excessive network requests.
 */
public class CacheManager {

  private LoadingCache<String, List<List<String>>> cache;

  /**
   * @param size type int representing up to what size should cache store info
   * @param expireAfterWriteDuration type int representing up to how much time should cache store
   *     info
   */
  public CacheManager(int size, int expireAfterWriteDuration) {
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(size)
            .expireAfterWrite(expireAfterWriteDuration, TimeUnit.MINUTES)
            .build(
                new CacheLoader<String, List<List<String>>>() {
                  /**
                   * loads data to cache
                   *
                   * @param key type string of key in map
                   * @return list of list of strings of cached data
                   * @throws Exception if URL connections throw an error
                   */
                  @Override
                  public List<List<String>> load(String key) throws Exception {
                    URL requestURL = new URL("https", "api.census.gov", key);
                    HttpURLConnection clientConnection =
                        (HttpURLConnection) requestURL.openConnection();
                    clientConnection.setRequestMethod("GET");

                    Moshi moshi = new Moshi.Builder().build();
                    Type listListStringType =
                        Types.newParameterizedType(List.class, List.class, String.class);
                    JsonAdapter<List<List<String>>> jsonAdapter = moshi.adapter(listListStringType);

                    // Use BufferedReader to read the InputStream
                    try (BufferedReader reader =
                        new BufferedReader(
                            new InputStreamReader(clientConnection.getInputStream()))) {
                      List<List<String>> data =
                          jsonAdapter.fromJson(
                              new Buffer().readFrom(clientConnection.getInputStream()));
                      if (data == null) {
                        throw new DataSourceException("No data received from Census API");
                      }
                      return data;
                    } finally {
                      clientConnection.disconnect();
                    }
                  }
                });
  }

  /**
   * retrieves value from cache
   *
   * @param key type string of key to search for
   * @return list of list of strings representing cached value
   */
  public Object getFromCache(String key) {
    try {
      return cache.get(key);
    } catch (Exception e) {
      return e.getMessage();
    }
  }

  /**
   * inputs data in cache
   *
   * @param key type string of key to search for
   * @param value type list of list of strings representing value in cache
   */
  public void putInCache(String key, List<List<String>> value) {
    cache.put(key, value);
  }
}
