package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSV.Parse;
import edu.brown.cs.student.main.CSV.Search;
import edu.brown.cs.student.main.Creator.Creator;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  /**
   * @param args - input of array of strings
   */
  private Main(String[] args) {
  }

  private void run() {
    try {
      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter CSV data source (filename string or Reader object): ");
      String data = scanner.nextLine();
      System.out.println("Enter filepath of CSV data source (no if no filepath): ");
      String path = scanner.nextLine();
      System.out.println("Enter whether CSV contains headers (yes/no): ");
      boolean hasHeaders = scanner.nextLine().trim().equalsIgnoreCase("yes");
      System.out.println("Enter value to search for in CSV: ");
      String value = scanner.nextLine();
      System.out.println("Enter column identifier to narrow search (no if no identifier): ");
      String colIdentifier = scanner.nextLine();

      if (data.startsWith("FileReader(")) {
        Reader new_data = new FileReader(path);
        Creator creator = new Creator();
        Parse parse = new Parse(new_data, hasHeaders, creator, path);
        if (Objects.equals(colIdentifier, "no")) {
          Search search = new Search(parse, value);
          System.out.println(search.search());
        } else {
          if (isNumber(colIdentifier)) {
            int columnIndex = Integer.parseInt(colIdentifier);
            Search search = new Search(parse, value, columnIndex);
            System.out.println(search.search());
          } else {
            Search search = new Search(parse, value, colIdentifier);
            System.out.println(search.search());
          }
        }
      } else if (data.startsWith("StringReader(")) {
        String content = data.substring(data.indexOf('(') + 1, data.lastIndexOf(')'))
            .replace("\"", "");
        Reader new_data = new StringReader(content);
        Creator creator = new Creator();
        Parse parse = new Parse(new_data, hasHeaders, creator, path);
        if (Objects.equals(colIdentifier, "no")) {
          Search search = new Search(parse, value);
          System.out.println(search.search());
        } else {
          if (isNumber(colIdentifier)) {
            int columnIndex = Integer.parseInt(colIdentifier);
            Search search = new Search(parse, value, columnIndex);
            System.out.println(search.search());
          } else {
            Search search = new Search(parse, value, colIdentifier);
            System.out.println(search.search());
          }
        }
      } else {
        Creator creator = new Creator();
        Parse parse = new Parse(data, hasHeaders, creator, path);
        if (Objects.equals(colIdentifier, "no")) {
          Search search = new Search(parse, value);
          System.out.println(search.search());
        } else {
          if (isNumber(colIdentifier)) {
            int columnIndex = Integer.parseInt(colIdentifier);
            Search search = new Search(parse, value, columnIndex);
            System.out.println(search.search());
          } else {
            Search search = new Search(parse, value, colIdentifier);
            System.out.println(search.search());
          }
        }

      }

    } catch (Exception e) {
      System.err.println("An error occurred: " + e.getMessage());
    }
  }

  /**
   * @param str - input of type String
   * @return - output of boolean indicating Number
   */
  private static boolean isNumber(String str) {
    try {
      Integer.parseInt(str);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
