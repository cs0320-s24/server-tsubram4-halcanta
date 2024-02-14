package edu.brown.cs.student.main.Creator;

import java.util.List;

/** Creator class allows developer to convert each row into a desired object */
public class Creator implements CreatorFromRow<List<String>> {

  /**
   * @param row - input of type List<String>
   * @return - outputs List<String> or another desired object
   * @throws FactoryFailureException - exception if row does not meet criteria
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    if (row.isEmpty()) {
      throw new FactoryFailureException("Row cannot be empty", row);
    }
    return row;
  }
}
