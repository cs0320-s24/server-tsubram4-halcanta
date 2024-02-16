package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import java.util.ArrayList;
import java.util.List;
import spark.Spark;

public class Server {
  private final CensusDataSource source;

  public Server(CensusDataSource s) {
    this.source = s;
    int port = 3232;
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    /** String somethingSomethingAsJson = somethingAPIUtilities.readInJson("a filepath"); */
    List<Object> data = new ArrayList<>();

    try {
      /** data = somethingAPIUtilities.readInJason(somethingSomethingAsJson); */
    } catch (Exception e) {
      // do not want to keep this broad exception, but it should help when it comes to debugging
      e.printStackTrace();
      System.err.println("Error message blah blah blah");
    }

    Spark.get("", new CensusHandler(s));
    // Spark.get("path name", new anotherTypeOfHandler);
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at https://localhost:" + port);
  }

  public static void main(String[] args) {

    Server server = new Server(new APICensusDataSource());
  }
}
