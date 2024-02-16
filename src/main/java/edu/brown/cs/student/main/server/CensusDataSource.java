package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.DataSource.DataSourceException;

import java.io.IOException;
import java.net.MalformedURLException;

public interface CensusDataSource {

  BroadbandData getBroadbandData(String state, String county) throws DataSourceException, IOException;
}
