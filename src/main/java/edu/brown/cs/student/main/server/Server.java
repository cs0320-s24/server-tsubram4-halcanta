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

    Spark.get("broadband", new CensusHandler(s));
    Spark.get("csv", new RequestHandler());
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }

  public static void main(String[] args) {

    Server server = new Server(new APICensusDataSource());
  }
}
