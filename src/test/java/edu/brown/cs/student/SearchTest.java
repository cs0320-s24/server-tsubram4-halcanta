package edu.brown.cs.student;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.main.CSV.Parse;
import edu.brown.cs.student.main.CSV.Search;
import edu.brown.cs.student.main.Creator.Creator;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** Class to test search method */
public class SearchTest {

  private Parse parseWithHeader;
  private Parse parseWithHeaderProtected;
  private Parse parseWithoutHeader;
  private Parse parseFileReaderWithHeader;
  private Parse parseFileReaderWithoutHeader;
  private Parse parseStringReaderWithHeader;
  private Parse parseStringReaderWithoutHeader;

  /**
   * @throws Exception - exception if setup data is incorrect
   */
  @Before
  public void setUp() throws Exception {
    String csvWithHeader = "data/census/dol_ri_earnings_disparity.csv";
    String csvWithHeaderProtected =
        "src/main/java/edu/brown/cs/student/main/protected.csv"; // DOESNT WORK IN MAIN******
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
   * testing search method on data without column identifier
   *
   * @throws Exception - if search method throws an exception
   */
  @Test
  public void testSearchWithoutColIdentifier() throws Exception {
    Search search = new Search(parseWithHeader, "Black");
    List<List<String>> results = search.search();
    assertEquals(1, results.size());
    assertEquals("[[RI, Black, $770.26, 30424.80376, $0.73, 6%]]", results.toString());

    Search search2 = new Search(parseWithoutHeader, "CHEM");
    List<List<String>> results2 = search2.search();
    assertEquals(2, results2.size());
    assertEquals("[[Jonny, CHEM, 85, B], [Jimmy, CHEM, 83, B]]", results2.toString());

    Search search3 = new Search(parseFileReaderWithHeader, "1");
    List<List<String>> results3 = search3.search();
    assertEquals(5, results3.size());

    Search search4 = new Search(parseFileReaderWithoutHeader, "A");
    List<List<String>> results4 = search4.search();
    assertEquals(2, results4.size());

    Search search5 = new Search(parseStringReaderWithHeader, "/");
    List<List<String>> results5 = search5.search();
    assertEquals(2, results5.size());

    Search search6 = new Search(parseStringReaderWithoutHeader, "y");
    List<List<String>> results6 = search6.search();
    assertEquals(4, results6.size());
  }

  /**
   * testing search method on data with column identifier
   *
   * @throws Exception - if search method throws an exception
   */
  @Test
  public void testSearchWithColIdentifier() throws Exception {
    Search search = new Search(parseWithHeader, "Black", "Data Type");
    List<List<String>> results = search.search();
    assertEquals(1, results.size());
    assertEquals("[[RI, Black, $770.26, 30424.80376, $0.73, 6%]]", results.toString());

    Search search3 = new Search(parseFileReaderWithHeader, "1", "Average Weekly Earnings");
    List<List<String>> results3 = search3.search();
    assertEquals(5, results3.size());

    Search search5 = new Search(parseStringReaderWithHeader, "/", "Data Type");
    List<List<String>> results5 = search5.search();
    assertEquals(2, results5.size());
  }

  /**
   * testing search method on small data of type StringReader
   *
   * @throws Exception - if search method throws an exception
   */
  @Test
  public void testSmallStringReader() throws Exception {
    Creator creator = new Creator();
    Reader smallStringReader = new StringReader("State,Data Type,Average Weekly Earnings");
    Parse parse = new Parse(smallStringReader, false, creator, "no");
    Search search = new Search(parse, "Data");
    System.out.println(search.search());
  }

  /**
   * testing search method if value is not found
   *
   * @throws Exception - if value doesn't exist in data
   */
  @Test(expected = IOException.class)
  public void testValueNotFound() throws Exception {
    Search search = new Search(parseWithHeader, "hellooo", "Data Type");
    List<List<String>> results = search.search();
    assertEquals(0, results.size());

    Search search2 = new Search(parseWithoutHeader, "Bye123");
    List<List<String>> results2 = search.search();
    assertEquals(0, results2.size());
  }

  /**
   * testing search method if value is found in different column
   *
   * @throws Exception - if value exists in column separate from colIdentifier
   */
  @Test
  public void testValueFoundInOtherColumn() throws Exception {
    Search search = new Search(parseWithHeader, "Black", "State");
    List<List<String>> results = search.search();
    assertEquals(0, results.size());

    Search search2 = new Search(parseFileReaderWithHeader, "2%", "Data Type");
    List<List<String>> results2 = search2.search();
    assertEquals(0, results2.size());
  }

  /**
   * testing search method if incorrect colIdentifier is used
   *
   * @throws Exception - if wrong column identifier is inputted
   */
  @Test(expected = IOException.class)
  public void testWrongColIdentifier() throws Exception {
    Search search = new Search(parseWithHeader, "Black", "asdsuydgobfuy");
    List<List<String>> results = search.search();
    assertEquals(0, results.size());

    Search search2 = new Search(parseWithoutHeader, "CHEM", 10);
    List<List<String>> results2 = search.search();
    assertEquals(0, results2.size());
  }
}
