package edu.brown.cs.student;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CensusDatum.CensusDatumCreator;
import edu.brown.cs.student.main.CreatorFromRow.FactoryFailureException;
import edu.brown.cs.student.main.Recipe.RecipeCreator;
import edu.brown.cs.student.main.Star.StarCreator;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

/** Contains tests for the CSVSearch class */
public class CSVSearchTest {

  @Before
  public void setUp() {}

  @After
  public void tearDown() {}

  @Test
  public void testSearchValuesNotPresent() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/stars/stardata.csv";
    String wordTS = "steak";
    String sType = "EntireFile";
    FileReader rTBP = new FileReader(fName);
    RecipeCreator cStars = new RecipeCreator();

    fileParser.parse(fName, wordTS, true, -1, sType, "", rTBP, cStars);

    Assert.assertEquals(0, fileParser.getWillSearch().getNumberOfMatches());
  }

  @Test
  public void testSearchValuesPresent() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/census/income_by_race.csv";
    String wordTS = "asian";
    String sType = "EntireFile";
    FileReader rTBP = new FileReader(fName);
    CensusDatumCreator cCD = new CensusDatumCreator();

    fileParser.parse(fName, wordTS, true, -1, sType, "", rTBP, cCD);

    Assert.assertEquals(40, fileParser.getWillSearch().getNumberOfMatches());
  }

  @Test
  public void testSearchByIndex() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/census/income_by_race.csv";
    String wordTS = "White Non-Hispanic";
    String sType = "ColumnIndex";
    FileReader rTBP = new FileReader(fName);
    CensusDatumCreator cCD = new CensusDatumCreator();

    fileParser.parse(fName, wordTS, true, 1, sType, "", rTBP, cCD);

    Assert.assertEquals(40, fileParser.getWillSearch().getNumberOfMatches());
  }

  @Test
  public void testSearchByColHeader() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName =
        "ID Race,Race,ID Year,Year,Household Income by Race,Household Income by Race Moe,Geography,ID Geography,Slug Geography\n"
            + "0,Total,2020,2020,85413,6122,\"Bristol County, RI\",05000US44001,bristol-county-ri\n"
            + "1,White,2020,2020,67639,1255,\"Providence County, RI\",05000US44007,providence-county-ri\n"
            + "2,Black,2020,2020,45849,6614,\"Washington County, RI\",05000US44009,washington-county-ri";
    String wordTS = "White";
    String sType = "ColumnHeader";
    StringReader rTBP = new StringReader(fName);
    CensusDatumCreator cCD = new CensusDatumCreator();

    fileParser.parse(fName, wordTS, true, 1, sType, "", rTBP, cCD);

    Assert.assertEquals(1, fileParser.getWillSearch().getNumberOfMatches());
  }

  @Test
  public void testSearchByEntireFile() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName =
        "ID Race,Race,ID Year,Year,Household Income by Race,Household Income by Race Moe,Geography,ID Geography,Slug Geography\n"
            + "0,Total,2020,2020,85413,6122,\"Bristol County, RI\",05000US44001,bristol-county-ri\n"
            + "1,White,2020,2020,67639,1255,\"Providence County, RI\",05000US44007,providence-county-ri\n"
            + "2,Black,2020,2020,45849,6614,\"Washington County, RI\",05000US44009,washington-county-ri";
    String wordTS = "2020";
    String sType = "EntireFile";
    StringReader rTBP = new StringReader(fName);
    CensusDatumCreator cCD = new CensusDatumCreator();

    fileParser.parse(fName, wordTS, true, -1, sType, "", rTBP, cCD);

    Assert.assertEquals(6, fileParser.getWillSearch().getNumberOfMatches());
  }

  @Test
  public void testCreatorFromRowListOfString() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/recipe/meemaws_recipe_book.csv";
    String wordTS = "";
    String sType = "EntireFile";
    FileReader rTBP = new FileReader(fName);
    RecipeCreator cRC = new RecipeCreator();

    fileParser.parse(fName, wordTS, false, -1, sType, "", rTBP, cRC);

    Assert.assertEquals(10, fileParser.getWillSearch().getCreatedItems().size());
  }

  @Test
  public void testMalformedRows() throws IOException, FactoryFailureException {
    CSVParser fileParser = new CSVParser();
    String fName = "data/stars/ten-star.csv";
    String wordTS = "Sol";
    String sType = "EntireFile";
    FileReader rTBP = new FileReader(fName);
    StarCreator cRC = new StarCreator();

    fileParser.parse(fName, wordTS, true, -1, sType, "", rTBP, cRC);

    Assert.assertEquals(6, fileParser.getWillSearch().getCreatedItems().size());
  }
}
