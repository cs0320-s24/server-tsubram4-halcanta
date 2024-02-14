package edu.brown.cs.student.main.Creator;

import java.util.List;

/** DataCreator class is an example of an object that each row can be converted to as a Creator */
public class DataCreator implements CreatorFromRow<DataCreator> {

  private String entry;

  /**
   * @param entry - input of type String
   */
  public DataCreator(String entry) {
    this.entry = entry;
  }

  /**
   * @param row - input row of type List<String>
   * @return - DataCreator representing each row
   * @throws FactoryFailureException - exception if row does not meet criteria
   */
  @Override
  public DataCreator create(List<String> row) throws FactoryFailureException {
    if (row.isEmpty()) {
      throw new FactoryFailureException("Row cannot be empty", row);
    }
    String argument = String.join("", row).trim();
    if (argument.isEmpty()) {
      throw new FactoryFailureException("Row content cannot be only whitespace", row);
    }
    return new DataCreator(argument);
  }

  /**
   * @return - String of entry field
   */
  public String getEntry() {
    return this.entry;
  }
}
