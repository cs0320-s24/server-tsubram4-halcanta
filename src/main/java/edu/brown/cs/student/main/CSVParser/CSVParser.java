package edu.brown.cs.student.main.CSVParser;

import edu.brown.cs.student.main.CSVSearch.CSVSearch;
import edu.brown.cs.student.main.CreatorFromRow.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import edu.brown.cs.student.main.Main;
import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This is the Parser class of our project. This is where the files are parsed, and passed onto the
 * Search class of our project.
 */
public class CSVParser<T> {
  private CSVSearch willSearch;

  public CSVSearch getWillSearch() {
    return willSearch;
  }

  /**
   * The parse method does exactly that: parse a file. However, since it can take in an object of
   * the type Reader, it can also parse a string!
   *
   * @param fileName - the name of the file which should be parsed (or the string that should be
   *     parsed in the case of StringReader
   * @param wordToSearch - the word you would like to search for within your file. Note that the
   *     case (uppercase/ lowercase) should not matter, but the word must match exactly (ex:
   *     searching for the word 'bear' will not match to 'big bear'
   * @param hasHeader - a boolean which is a placeholder for the indentification of the existence of
   *     a header row within the file you wish to parse
   * @param columnIndex - this integer represents the position of the column you would like to
   *     search through
   * @param altSearchMethod - I was hoping for this to be an enum that matched a specific search
   *     method (by index, column title, etc), however, I ran out of time. Now, this parameter is a
   *     string that represents the search method that will be used.
   * @param columnHeaderToSearch - In the case that you do want to search via headers, this
   *     parameter will store the name of the header you wish to focus on.
   * @param fReader - an object of the Reader class, needed in order to read your input file
   * @param willCreate - an Object that extends the CreatorFromRow interface. This method should
   *     transform each row of data within the CSV file into an object of your choice!
   * @return - This method will return a list containing the elements that are constructed from the
   *     willCreate parameter (i.e. your class that implements CreatorFromRow.
   * @throws IOException
   * @throws FactoryFailureException
   * @throws FileNotFoundException
   */
  public List parse(
      String fileName,
      String wordToSearch,
      Boolean hasHeader,
      int columnIndex,
      String altSearchMethod,
      String columnHeaderToSearch,
      Reader fReader,
      CreatorFromRow<T> willCreate)
      throws IOException, FactoryFailureException, FileNotFoundException {

    final Pattern rgxSplitCSVRow =
        Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

    CSVSearch searchAlgorithm = new CSVSearch();
    searchAlgorithm.createCSVSearch();
    this.willSearch = searchAlgorithm;

    try {
      BufferedReader buffReader = new BufferedReader(fReader);
      String csvRow = buffReader.readLine();
      int numberOfHeadings = 0;

      if (hasHeader) {
        String[] tempResult = rgxSplitCSVRow.split(csvRow);
        numberOfHeadings = tempResult.length;
        for (int i = 0; i < tempResult.length; i++) {
          if (tempResult[i].equalsIgnoreCase(columnHeaderToSearch)) {
            columnIndex = i;
          }
        }

        if (columnIndex == -1 && !altSearchMethod.equals("EntireFile")) {
          throw new InvalidObjectException("Desired column header could not be found.");
        }

        csvRow = buffReader.readLine();
      } else {
        String[] tempResult = rgxSplitCSVRow.split(csvRow);
        numberOfHeadings = tempResult.length;
      }

      while (csvRow != null) {
        String[] result = rgxSplitCSVRow.split(csvRow);

        searchAlgorithm.search(result, wordToSearch, columnIndex, numberOfHeadings, willCreate);
        csvRow = buffReader.readLine();
      }

      buffReader.close();

    } catch (InvalidObjectException e) {
      System.err.println(
          "Desired column header could not be found, please ensure the "
              + "column you would like to search exists. Now rebooting program...");
      String[] restart = new String[1];
      Main.main(restart);
    } catch (IOException e) {
      System.err.println(
          "Sorry, your file could not be found! "
              + "Please ensure your file is located in the correct directory.");
      System.exit(0);
    }

    System.out.print(
        "There were "
            + searchAlgorithm.getNumberOfMatches()
            + " matches found within "
            + "the file!");
    return searchAlgorithm.getCreatedItems();
  }
}
