package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class CensusHandler implements Route {

  private final CensusDataSource source;

  public CensusHandler(CensusDataSource s) {
    this.source = s;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();

    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
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

    // continue with the rest of the request
    return null;
  }
}
