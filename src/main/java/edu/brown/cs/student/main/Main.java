package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CensusDatum.CensusDatum;
import edu.brown.cs.student.main.CensusDatum.CensusDatumCreator;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import edu.brown.cs.student.main.Star.StarCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments you are given by the reader; parse based on
   *     something given, creatorFromRow object
   */
  public static void main(String[] args) throws IOException, FactoryFailureException {
    new Main(args).run();
  }

  private Main(String[] args) {}

  /**
   * The bread and butter of this project. Through the Main.run() method, the project is able to
   * take in user input through the terminal, and utilize it to identify the words they would like
   * to search for, the file they would like to parse, and the method for which they would like to
   * search.
   *
   * @throws FactoryFailureException
   * @throws IOException
   * @throws FileNotFoundException - in the case of this exception, the main method will simply
   *     reboot
   */
  private void run() throws FactoryFailureException, IOException, FileNotFoundException {
    Scanner scanner = new Scanner(System.in);
    System.out.println(
        "Hello. Please enter the file name of the file you wish to read. Please note that "
            + "the file must exist solely within the data folder.");
    String filename = scanner.nextLine();
    String filepath = "data/" + filename + ".csv";
    String searchMethod = "";

    System.out.println("Does your file contain headers: (true/false)");
    Boolean containsHeaderBool = scanner.nextBoolean();

    scanner.nextLine();

    int columnIndex = -1;
    String columnHeaderToSearch = "";

    if (containsHeaderBool) {
      System.out.println(
          "Please enter your preferred search method: (ColumnIndex/ColumnHeader/EntireFile)");
      searchMethod = scanner.nextLine();

      switch (searchMethod) {
        case ("ColumnHeader") -> {
          System.out.println(
              "Please enter the header name of the column you would like to search: ");
          columnHeaderToSearch = scanner.nextLine();
        }
        case ("ColumnIndex") -> {
          System.out.println(
              "Please enter the index of the column you would like to search. Note that "
                  + "the first column has an index of 0, and so on...");
          columnIndex = scanner.nextInt();
          scanner.nextLine();
        }
        case ("EntireFile") -> {
          System.out.println("Great, we will search the entire file!");
        }
      }
    } else {
      System.out.println("Please enter your preferred search method: (ColumnIndex/EntireFile)");
      searchMethod = scanner.nextLine();

      switch (searchMethod) {
        case ("ColumnIndex") -> {
          System.out.println(
              "Please enter the index of the column you would like to search. Note that "
                  + "the first column has an index of 0, and so on...");
          columnIndex = scanner.nextInt();
          scanner.nextLine();
        }
        case ("EntireFile") -> {
          System.out.println("Great, we will search the entire file!");
        }
      }
    }

    System.out.println("Please enter the value you would like to search for:");
    String searchWord = scanner.nextLine();

    /** INSERT YOUR OWN OBJECT THAT EXTENDS THE READER CLASS HERE */
    Reader fReader = null;
    try {
      fReader = new FileReader(filepath);

      /** INSERT YOUR OWN OBJECT THAT EXTENDS THE READER CLASS HERE */
      CensusDatumCreator willCreate = new CensusDatumCreator();

      CSVParser fileParser = new CSVParser();
      fileParser.parse(
          filepath,
          searchWord,
          containsHeaderBool,
          columnIndex,
          searchMethod,
          columnHeaderToSearch,
          fReader,
          willCreate);
    } catch (FileNotFoundException e) {
      System.err.println(
          "Sorry, your file could not be found! "
              + "Please ensure your file is located solely in the correct directory.");
      System.err.println("Now restarting the program...");
      String[] restart = new String[1];
      Main.main(restart);
    }
  }
}
