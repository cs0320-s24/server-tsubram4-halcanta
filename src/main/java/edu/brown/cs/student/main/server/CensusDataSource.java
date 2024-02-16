package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.DataSource.DataSourceException;
import java.io.IOException;

public interface CensusDataSource {

  BroadbandData getBroadbandData(String state, String county)
      throws DataSourceException, IOException;
}
