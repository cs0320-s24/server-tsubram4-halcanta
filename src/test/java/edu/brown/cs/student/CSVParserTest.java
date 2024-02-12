package edu.brown.cs.student;

import static org.junit.Assert.assertThrows;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import edu.brown.cs.student.main.Recipe.RecipeCreator;
import edu.brown.cs.student.main.Star.StarCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

/** Contains tests for the CSVSParser class */
public class CSVParserTest {
  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testCSVWithInconsistentColumnCount() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/recipe/meemaws_recipe_book_inconsistent.csv";
    String wordTS = "";
    String sType = "EntireFile";
    FileReader rTBP = new FileReader(fName);
    RecipeCreator cRC = new RecipeCreator();

    List<List<String>> pRslt = fileParser.parse(fName, wordTS, false, -1, sType, "", rTBP, cRC);

    int target = pRslt.size();
    List<String> targetList = new ArrayList<>();
    targetList.add("Milk");
    targetList.add("Sugar");
    targetList.add("Butter");
    targetList.add("Salt");

    Assert.assertEquals(6, target);
    Assert.assertTrue(pRslt.contains(targetList));
  }

  @Test
  public void testFileNotFoundException() throws FileNotFoundException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/stars/top_ten_biggest_hollywood_stars.csv";
    String wordTS = "Sol";
    String sType = "EntireFile";
    StarCreator cRC = new StarCreator();

    assertThrows(
        FileNotFoundException.class,
        () -> {
          fileParser.parse(fName, wordTS, true, -1, sType, "", new FileReader(fName), cRC);
        });
  }
}
