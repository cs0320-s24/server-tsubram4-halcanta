package edu.brown.cs.student;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.main.CSVHandler.CSV.Parse;
import edu.brown.cs.student.main.CSVHandler.Creator.Creator;
import edu.brown.cs.student.main.CSVHandler.Creator.CreatorFromRow;
import edu.brown.cs.student.main.CSVHandler.Creator.DataCreator;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

/** ParseTest tests the Parse class */
public class ParseTest {

  private Parse parseWithHeader;
  private Parse parseWithoutHeader;
  private Parse parseFileReaderWithHeader;
  private Parse parseFileReaderWithoutHeader;
  private Parse parseStringReaderWithHeader;
  private Parse parseStringReaderWithoutHeader;

  /**
   * @throws Exception - if setup data is incorrect
   */
  @Before
  public void setUp() throws Exception {
    String csvWithHeader = "data/census/dol_ri_earnings_disparity.csv";
    String csvWithoutHeader = "data/grade_data.csv";
    Reader csvFileReaderWithHeader = new FileReader("data/census/dol_ri_earnings_disparity.csv");
    Reader csvFileReaderWithoutHeader = new FileReader("data/grade_data.csv");
    Reader csvStringReaderWithHeader =
        new StringReader(
            "State,Data Type,Average Weekly Earnings,Number of Workers,Earnings Disparity,Employed Percent\n"
                + "RI,White,\" $1,058.47 \",395773.6521, $1.00 ,75%\n"
                + "RI,Black, $770.26 ,30424.80376, $0.73 ,6%\n"
                + "RI,Native American/American Indian, $471.07 ,2315.505646, $0.45 ,0%\n"
                + "RI,Asian-Pacific Islander,\" $1,080.09 \",18956.71657, $1.02 ,4%\n"
                + "RI,Hispanic/Latino, $673.14 ,74596.18851, $0.64 ,14%\n"
                + "RI,Multiracial, $971.89 ,8883.049171, $0.92 ,2%");
    Reader csvStringReaderWithoutHeader =
        new StringReader(
            "Bobby, BIOL, 92, A\n"
                + "Jonny, CHEM, 85, B\n"
                + "Sara, ANTH, 90, A\n"
                + "Jimmy, CHEM, 83, B\n"
                + "Yatty, CSCI, 77, C");

    Creator creator = new Creator();
    parseWithHeader = new Parse(csvWithHeader, true, creator, csvWithHeader);
    parseWithoutHeader = new Parse(csvWithoutHeader, false, creator, csvWithoutHeader);
    parseFileReaderWithHeader =
        new Parse(
            csvFileReaderWithHeader, true, creator, "data/census/dol_ri_earnings_disparity.csv");
    parseFileReaderWithoutHeader =
        new Parse(csvFileReaderWithoutHeader, false, creator, "data/grade_data.csv");
    parseStringReaderWithHeader = new Parse(csvStringReaderWithHeader, true, creator, "no");
    parseStringReaderWithoutHeader = new Parse(csvStringReaderWithoutHeader, false, creator, "no");
  }

  /**
   * testing parse method on data with headers
   *
   * @throws Exception - if parse method throws an exception
   */
  @Test
  public void testParseWithHeaders() throws Exception {
    List<List<String>> records = parseWithHeader.parse();
    assertEquals(6, records.size());

    List<List<String>> records2 = parseFileReaderWithHeader.parse();
    assertEquals(6, records2.size());

    List<List<String>> records3 = parseStringReaderWithHeader.parse();
    assertEquals(6, records3.size());

    assertEquals(records, records2);
    assertEquals(records2, records3);
  }

  /**
   * testing parse method on data without headers
   *
   * @throws Exception - if parse method throws an exception
   */
  @Test
  public void testParseWithoutHeaders() throws Exception {
    List<List<String>> records = parseWithoutHeader.parse();
    assertEquals(5, records.size());

    List<List<String>> records2 = parseFileReaderWithoutHeader.parse();
    assertEquals(5, records2.size());

    List<List<String>> records3 = parseStringReaderWithoutHeader.parse();
    assertEquals(5, records3.size());

    assertEquals(records, records2);
    assertEquals(records2, records3);
  }

  /**
   * testing parse method on protected data
   *
   * @throws Exception - if file is protected and cannot be accessed
   */
  @Test(expected = IllegalArgumentException.class)
  public void testProtectedFileError() throws Exception {
    Creator creator = new Creator();
    String path = "src/main/java/edu/brown/cs/student/main/ProtectedData/protected.csv";
    Reader read = new FileReader(path);
    Parse parseFun = new Parse(read, true, creator, path);
    parseFun.parse();

    Reader read2 = new StringReader(path);
    Parse parseFun2 = new Parse(read2, true, creator, path);
    parseFun2.parse();

    Parse parseFun3 = new Parse(path, true, creator, path);
    parseFun3.parse();
  }

  /**
   * testing parse method on unsupported file data
   *
   * @throws Exception - if file is unsupported
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUnsupportedFileType() throws Exception {
    Creator creator = new Creator();
    String path = "data/grade_data.csv";
    Reader read = new FileReader(path);
    Parse parseFun = new Parse(read.read(), true, creator, path);
    parseFun.parse();
  }

  /**
   * testing parse method on data with inconsistent column counts
   *
   * @throws Exception - if data has inconsistent columns
   */
  @Test(expected = IOException.class)
  public void testInconsistentColumnCount() throws Exception {
    Creator creator = new Creator();
    String path = "data/malformed/malformed_signs.csv";
    Parse parseFun = new Parse(path, true, creator, path);
    parseFun.parse();
  }

  /**
   * testing parse method with various creators
   *
   * @throws Exception - if parse method throws an exception
   */
  @Test
  public void testCreators() throws Exception {
    /** Converts each row to a String */
    class StringCreator implements CreatorFromRow<String> {

      /**
       * @param row - input row of type List<String>
       * @return - outputs row as String
       */
      @Override
      public String create(List<String> row) {
        return String.join("", row);
      }
    }

    StringCreator creator1 = new StringCreator();
    String path = "data/grade_data.csv";
    Parse parseFun = new Parse(path, false, creator1, path);
    assertEquals(
        List.of("BobbyBIOL92A", "JonnyCHEM85B", "SaraANTH90A", "JimmyCHEM83B", "YattyCSCI77C"),
        parseFun.parse());

    /** Converts each row to the sum of the numbers in that row */
    class IntegerCreator implements CreatorFromRow<Integer> {

      /**
       * @param row - input row of type List<String>
       * @return - Integer indicating sum of numbers in that row
       */
      @Override
      public Integer create(List<String> row) {
        int sum = 0;
        for (String element : row) {
          try {
            sum += Integer.parseInt(element.trim());
          } catch (NumberFormatException e) {
            sum += 0;
          }
        }
        return sum;
      }
    }

    IntegerCreator creator2 = new IntegerCreator();
    Parse parseFun2 = new Parse(path, false, creator2, path);
    assertEquals(List.of(92, 85, 90, 83, 77), parseFun2.parse());

    String csvContent = "Column1,Column2\nValue1,Value2\nValue3,Value4";
    Reader csvStringReader = new StringReader(csvContent);
    DataCreator dataCreator = new DataCreator("");
    Parse<DataCreator> parseWithDataCreator = new Parse<>(csvStringReader, true, dataCreator, "no");
    List<DataCreator> parsedData = parseWithDataCreator.parse();

    // Convert list of Data objects to list of strings for easy comparison
    List<String> parsedStrings =
        parsedData.stream().map(data -> data.getEntry()).collect(Collectors.toList());

    List<String> expected = List.of("Value1Value2", "Value3Value4");
    assertEquals(expected, parsedStrings);
  }

  /**
   * testing incorrect regex
   *
   * @throws Exception - exception if parse method throws an exception
   */
  @Test
  public void testRegexError() throws Exception {
    Reader file = new FileReader("data/specialchar.csv");
    Creator creator = new Creator();
    Parse parse = new Parse(file, false, creator, "data/specialchar.csv");
    System.out.println(parse.parse());
  }
}
