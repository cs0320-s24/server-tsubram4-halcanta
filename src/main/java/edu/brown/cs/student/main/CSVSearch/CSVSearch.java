package edu.brown.cs.student.main.CSVSearch;

import edu.brown.cs.student.main.CreatorFromRow.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the Search class of our project. This is where the parsed rows come to be searched
 * through!
 */
public class CSVSearch<T> {

  private int numberOfMatches;

  private List<T> createdItems;

  /**
   * This method instantiates the private fields of the CSVSearch method. Must be called after
   * instantiating an object of the CSVSearch class
   */
  public void createCSVSearch() {
    this.numberOfMatches = 0;
    this.createdItems = new ArrayList<T>();
  }

  /**
   * A simple getter method for the number of matches that have taken place
   *
   * @return - an integer representing the total number of matches that have taken place while
   *     searching through parsed rows.
   */
  public int getNumberOfMatches() {
    return this.numberOfMatches;
  }

  /**
   * A simple getter method for the number of items that have been created, and placed within a list
   * of generic typing
   *
   * @return - a list containing all of the object created by the CreatorFromRow implementing class
   *     that was given as an argument
   */
  public List<T> getCreatedItems() {
    return this.createdItems;
  }

  /**
   * The actual search method. Goes through parsed rows and looks for a word in particular. If a
   * word within the parsed row matches the one we are looking for, it prints out the entirety of
   * the row, updates the match count, and passes the row onto the CreatorFromRow implementing
   * argument.
   *
   * @param currentRow - An array of strings representing the parsed file row
   * @param searchWord - The word we would like to search for
   * @param columnIndex - If the user decided to search a specific row in particular, this integer
   *     represents the index of the desired column
   * @param numberOfHeadings - This integer represents the number of headings the first row of the
   *     file contained. This number provides assistance when determining if a row is malformed
   *     (i.e. contains more or less columns than it should.
   * @param willCreate - the object that implements CreatorFromRow and will be converting the rows
   *     of data into other object.
   * @throws FactoryFailureException
   */
  public void search(
      String[] currentRow,
      String searchWord,
      int columnIndex,
      int numberOfHeadings,
      CreatorFromRow<T> willCreate)
      throws FactoryFailureException {

    try {
      if (currentRow.length > numberOfHeadings) {
        throw new ArithmeticException();
      }

      if (columnIndex == -1) {
        for (String s : currentRow) {
          if (s.equalsIgnoreCase(searchWord)) {
            this.numberOfMatches = this.numberOfMatches + 1;
            System.out.println(Arrays.toString(currentRow));
          } else if (s.equalsIgnoreCase("")) {
            throw new IllegalArgumentException();
          }
        }
      } else {
        if (currentRow[columnIndex].equalsIgnoreCase(searchWord)) {
          this.numberOfMatches = this.numberOfMatches + 1;
          System.out.println(Arrays.toString(currentRow));
        } else if (currentRow[columnIndex].equalsIgnoreCase("")) {
          throw new IllegalArgumentException();
        }
      }

      this.createdItems.add(willCreate.create(Arrays.asList(currentRow), numberOfHeadings));
    } catch (ArrayIndexOutOfBoundsException e) {
      System.err.println(
          "The following data row did not contain the sufficient number of columns to "
              + "reach the desired column index:");
      System.err.println(Arrays.toString(currentRow));
    } catch (IllegalArgumentException e) {
      System.err.println(
          "The following data row did not contain a value within at least one of its columns:");
      System.err.println(Arrays.toString(currentRow));
    } catch (ArithmeticException e) {
      System.err.println(
          "The following row had more columns than the number of headings within the file");
      System.err.println(Arrays.toString(currentRow));
    } catch (FactoryFailureException e) {
      System.err.println(
          "The following row could not be created into your desired object. "
              + "Please ensure it contains the correct number of items necessary for the object you would like to "
              + "create!");
      System.err.println(Arrays.toString(currentRow));
    }
  }
}
