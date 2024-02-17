package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.DataSource.DataSourceException;
import java.io.IOException;

/** This interface mandates the getBroadbandData method implemented by APICensusDataSource */
public interface CensusDataSource {

  /**
   * retrieves BroadbandData
   *
   * @param state type string of state to search for
   * @param county type string of county to search for
   * @return type BroadbandData
   * @throws DataSourceException if data is null
   * @throws IOException if URL connection has error
   */
  BroadbandData getBroadbandData(String state, String county)
      throws DataSourceException, IOException;
}
