// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import a2.Cd;

public class CdTest {

  MockFileSystem mfs;
  Cd test;

  // Sets up a mock file system to test commands because it relies on the file
  // system. Assuming that all these dependencies are completely functional
  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
  }

  // Destroys the mfs created for the test
  @After
  public void tearDown() throws Exception {
    Field field = (mfs.mfs.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    try {
      field.set(null, null);
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (IllegalAccessException e) {
      throw e;
    }
  }

  // Tests the cd constructor with empty input
  @Test
  public void constructorTest1() {
    String[] arguments = {};
    test = new Cd(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Tests the cd constructor with two inputs
  @Test
  public void constructorTest2() {
    String[] arguments = {">", "file"};
    test = new Cd(arguments);
    assertEquals(test.getArguments()[0], arguments[0]);
    assertEquals(test.getArguments()[1], arguments[1]);
  }

  /*
   * NOTE: Dependency injection cannot be applied for the execute method inside
   * the Cd class since it uses static methods from other classes. The test
   * cases below assume that the other methods it uses are completely functional
   */

  // Tests execute method current path is /dir1/dir11 and cd changes to /dir1
  // so the current pathname should be /dir1 (using absolute path)
  @Test
  public void executeTest1() {
    String[] arguments = {"/dir1"};
    test = new Cd(arguments);
    test.execute();
    String expected = "/dir1";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertEquals(expected, currentPath);
  }

  // Test execute method current path is /dir1/dir11 and cd changes to /dir2
  // so the current pathname should be /dir2 (using relative path)
  @Test
  public void executeTest2() {
    String[] arguments = {"../../dir2"};
    test = new Cd(arguments);
    test.execute();
    String expected = "/dir2";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertEquals(expected, currentPath);
  }

  // Test execute method for a invalid path so cd shouldn't change
  // to any directory
  @Test
  public void executeTest3() {
    String[] arguments = {"/invalid/path"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1/dir11";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertEquals(expected, currentPath);
    assertEquals(output, "cd: path does not exist");
  }

  // Test execute method for a valid path but into a file, error message should
  // be printed and current path should stay the same
  @Test
  public void executeTest4() {
    String[] arguments = {"/file1"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1/dir11";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, "cd: path is not a directory");
  }

  // Test execute method for a valid absolute path that contains ..
  @Test
  public void executeTest5() {
    String[] arguments = {"/dir1/dir11/../../dir2"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir2";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, null);
  }

  // Test execute method for a relative path with .. (changes to parent)
  @Test
  public void executeTest6() {
    String[] arguments = {".."};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, null);
  }

  // Test execute method with more than one argument, should stay at current
  // directory
  @Test
  public void executeTest7() {
    String[] arguments = {"first", "second"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1/dir11";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, null);
  }

  // Test execute with three valid arguments but cd only takes one argument
  // so expected to stay in current directory
  @Test
  public void executeTest8() {
    String[] arguments = {"/dir1", "/dir2", "/dir1/dir11"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1/dir11";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, null);
  }

  // Test a invalid path name
  @Test
  public void executeTest9() {
    String[] arguments = {"//dir1/"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1/dir11";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, null);
  }

  // changing into a path that specifies a file with absolute and contains ..
  // no change in current directory and output message path is not directory
  @Test
  public void executeTest10() {
    String[] arguments = {"/dir1/dir11/../file2"};
    test = new Cd(arguments);
    String output = test.execute();
    String expected = "/dir1/dir11";
    String currentPath = this.mfs.mfs.getCurrentDir().getPathname();
    assertTrue(expected.equals(currentPath));
    assertEquals(output, "cd: path is not a directory");
  }

}
