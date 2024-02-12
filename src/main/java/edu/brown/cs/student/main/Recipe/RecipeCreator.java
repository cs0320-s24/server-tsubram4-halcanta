package edu.brown.cs.student.main.Recipe;

import edu.brown.cs.student.main.CreatorFromRow.CreatorFromRow;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import java.util.ArrayList;
import java.util.List;

public class RecipeCreator implements CreatorFromRow<List<String>> {
  @Override
  public List<String> create(List<String> row, int requiredNumberOfFields)
      throws FactoryFailureException {
    List<String> recipeIngredients = new ArrayList<>();

    if (row.size() != requiredNumberOfFields) {
      throw new FactoryFailureException(
          "The following row of data could not be converted into a recipe", row);
    }

    recipeIngredients.addAll(row);

    return recipeIngredients;
  }
}
