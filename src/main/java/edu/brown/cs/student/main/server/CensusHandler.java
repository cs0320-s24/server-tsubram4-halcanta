package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.DataSource.DataSourceException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class handles requests from the CensusDataSource.
 */
public class CensusHandler implements Route {

  private final CensusDataSource source;

  /**
   * @param s type CensusDataSource of source of census
   */
  public CensusHandler(CensusDataSource s) {
    this.source = s;
  }

  /**
   * handles requests from CensusDataSource API
   *
   * @param request  type request representing user's request to API
   * @param response type response describing response to request
   * @return object converted to JSON
   * @throws Exception if data is null or connection throws error
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();

    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    //    Type recordStringString = Types.newParameterizedType(String.class, String.class);
    JsonAdapter<BroadbandData> broadBandDataAdaptor = moshi.adapter(BroadbandData.class);
    Map<String, Object> responseMap = new HashMap<>();

    String state = request.queryParams("state");
    String county = request.queryParams("county");

    if (state == null || county == null) {
      responseMap.put("query_state", state);
      responseMap.put("query_county", county);
      responseMap.put("type", "error");
      responseMap.put("error_type", "missing_parameter");
      responseMap.put("error_arg", state == null ? "state" : "county");
      return adapter.toJson(responseMap);
    }

    try {
      BroadbandData data = source.getBroadbandData(state, county);
      responseMap.put("type", "success");
      responseMap.put("requestedInfo", broadBandDataAdaptor.toJson(data));

      return adapter.toJson(responseMap);
    } catch (DataSourceException e) {
      responseMap.put("query_state", state);
      responseMap.put("query_county", county);
      responseMap.put("type", "error");
      responseMap.put("error_type", "datasource");
      responseMap.put("details", e.getMessage());
      return adapter.toJson(responseMap);
    } catch (IllegalArgumentException e) {
      responseMap.put("query_state", state);
      responseMap.put("query_county", county);
      responseMap.put("type", "error");
      responseMap.put("error_type", "bad_param");
      responseMap.put("details", e.getMessage());
      return adapter.toJson(responseMap);
    }
  }
}