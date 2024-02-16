package edu.brown.cs.student.main.CSVHandler.CSV;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Search class allows user to search for rows containing a value */
public class Search {

  private Parse data;
  private String value;
  private Object colIdentifier;

  /**
   * @param data - data file of type Parse
   * @param value - String user is searching for
   * @param colIdentifier - String or Integer column user is narrowing search to
   */
  public Search(
      Parse data, String value, Object colIdentifier) { // constructor if colIdentifier is provided
    this.data = data;
    this.value = value;
    if (!(colIdentifier instanceof String) && !(colIdentifier instanceof Integer)) {
      throw new IllegalArgumentException("colIdentifier must be either a String or an Integer");
    }
    this.colIdentifier = colIdentifier;
  }

  /**
   * @param data - data file of type Parse
   * @param value - String user is searching for
   */
  public Search(Parse data, String value) { // constructor if colIdentifier is not provided
    this.data = data;
    this.value = value;
    this.colIdentifier = null;
  }

  /**
   * @return - list of lists of strings containing value
   * @throws IOException - exception if value exists in different column or if value doesn't exist
   */
  public List<List<String>> search() throws IOException {
    List<List<String>> results = new ArrayList<>();
    List<List<String>> records = this.data.parse();
    int columnIndex = getColumnIndex();
    boolean valueFoundInDifferentColumn = false;

    for (List<String> record : records) {
      if (columnIndex != -1 && record.get(columnIndex).contains(value)) {
        results.add(record);
      } else if (columnIndex == -1) {
        for (String field : record) {
          if (field.contains(value)) {
            results.add(record);
            break;
          }
        }
      } else {
        // Check if value is present in different column
        for (int i = 0; i < record.size(); i++) {
          if (i != columnIndex && record.get(i).contains(value)) {
            valueFoundInDifferentColumn = true;
            break;
          }
        }
      }
    }

    if (results.isEmpty() && valueFoundInDifferentColumn) {
      System.err.println("The value exists in the CSV file, but not in the specified column.");
    } else if (results.isEmpty()) {
      throw new IOException(
          "Value does not exist in CSV file. Consider capitalization or a substring of the value.");
    }

    return results;
  }

  /**
   * @return - integer of column index
   * @throws IOException - exception if column identifier is incorrect
   */
  private int getColumnIndex() throws IOException {
    if (colIdentifier instanceof Integer) {
      Integer colIndex = (Integer) colIdentifier;
      if ((this.data.getHeaders().size() < colIndex) || (colIndex < 0)) {
        throw new IOException("Column identifier is out of the range");
      }
      return (Integer) colIdentifier;
    } else if (colIdentifier instanceof String) {
      if (!(this.data.getHeaders().contains(colIdentifier))) {
        throw new IOException("Column identifier is not a column in the CSV");
      }
      return this.data.getHeaders().indexOf(colIdentifier);
    }
    return -1; // Search in all columns if colIdentifier is null
  }
}
