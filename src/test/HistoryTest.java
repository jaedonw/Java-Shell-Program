// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import a2.History;
import junit.framework.Assert;

/**
 * @author Aum Patel
 *
 */
public class HistoryTest {

  History testHis;

  /**
   * Sets up the Linked list to contain the history.
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    this.testHis = new History();
    String[] args = {""};
    testHis.addHistory("ls", args);
    testHis.addHistory("popd", args);
    testHis.addHistory("rm", args);
    testHis.addHistory("history", args);
  }

  /**
   * test the addHistory method
   */
  @Test
  public void testAddHistory() {
    assertEquals(4, testHis.getHistory().size());
  }

  /**
   * Test the constructor that takes 2 arguments
   */
  @Test
  public void testHistory() {
    String[] arg = {""};
    History his2 = new History(arg, testHis);
    assertEquals(testHis.getHistory(), his2.getHistory());
  }

  /**
   * Test execution with no arguments
   */
  @Test
  public void testExecute1() {
    String[] arg = {};
    History his2 = new History(arg, testHis);
    String exp = "1. ls \n2. popd \n3. rm \n4. history \n";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with one valid argument
   */
  @Test
  public void testExecute2() {
    String[] arg = {"2"};
    History his2 = new History(arg, testHis);
    String exp = "3. rm \n4. history \n";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with one invalid argument (non integer)
   */
  @Test
  public void testExecute3() {
    String[] arg = {"hi"};
    History his2 = new History(arg, testHis);
    String exp = "Error! Invalid argument type. Should be integer.";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with one invalid integer argument
   */
  @Test
  public void testExecute4() {
    String[] arg = {"-2"};
    History his2 = new History(arg, testHis);
    String exp = "Error! Argument must be >= 0";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with 0 as argument
   */
  @Test
  public void testExecute5() {
    String[] arg = {"0"};
    History his2 = new History(arg, testHis);
    String exp = "";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with 2 arguments
   */
  @Test
  public void testExecute6() {
    String[] arg = {"3", "4"};
    History his2 = new History(arg, testHis);
    String exp = "Error! Usage: history (>> | >) filePath";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with a lot of arguments
   */
  @Test
  public void testExecute7() {
    String[] arg = {"3", "4", "4", "5"};
    History his2 = new History(arg, testHis);
    String exp = "Error! Usage: history [numOf] [[>|>>] filePath]";
    assertEquals(exp, his2.execute());
  }

  /**
   * Test execution with one valid, but huge argument
   */
  @Test
  public void testExecute8() {
    String[] arg = {"2222"};
    History his2 = new History(arg, testHis);
    String exp = "1. ls \n2. popd \n3. rm \n4. history \n";
    assertEquals(exp, his2.execute());
  }

}
