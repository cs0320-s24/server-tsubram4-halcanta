package edu.brown.cs.student.main.CSVHandler.Creator;

import java.util.List;

/**
 * This interface defines a method that allows your CSV parser to convert each row into an object of
 * some arbitrary passed type.
 *
 * <p>Your parser class constructor should take a second parameter of this generic interface type.
 */
public interface CreatorFromRow<T> {

  /**
   * @param row - input row of type List<String>
   * @return - outputs object of type T
   * @throws FactoryFailureException - throws error if row does not meet criteria
   */
  T create(List<String> row) throws FactoryFailureException;
}
