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
import a2.Echo;

public class EchoTest {
  Echo test;
  MockFileSystem mfs;

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

  /*
   * NOTE: Dependency injection cannot be applied for the constructor inside the
   * Echo class since it uses static methods from other classes. The test cases
   * below assume that the other methods it uses are completely functional
   */

  // Test the constructor for Echo with only the "string" argument
  @Test
  public void constructorTest1() {
    String[] arguments = {"contents", null, null};
    test = new Echo(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the constructor for Echo with three arguments
  @Test
  public void constructorTest2() {
    String[] arguments = {"with spaces", ">", "dog"};
    test = new Echo(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the constructor with path names that contains .. and . in relative
  @Test
  public void constructorTest3() {
    String[] arguments = {"with spaces", ">", "../first"};
    test = new Echo(arguments);
    assertEquals(test.getArguments()[0], "with spaces");
    assertEquals(test.getArguments()[1], ">");
    assertEquals(test.getArguments()[2], "/dir1/first");
  }

  /*
   * NOTE: Dependency injection cannot be applied for the execute method inside
   * the Echo class since it uses static methods from other classes. The test
   * cases below assume that the other methods it uses are completely functional
   */

  // Test the execute method with only the "string" argument
  // should return the "string" arguments
  @Test
  public void executeTest1() {
    String arguments[] = {"contents", null, null};
    test = new Echo(arguments);
    String output = test.execute();
    String expected = "contents";
    assertEquals(output, expected);
  }

  // Test the execute method with all arguments are null
  // should be an error and in the case of an error, executes return null
  @Test
  public void executeTest2() {
    String[] arguments = {null, null, null};
    test = new Echo(arguments);
    assertEquals(test.execute(), null);
  }

  // Test the execute method with redirection to an existing file, overwriting
  // its contents
  @Test
  public void executeTest3() {
    String[] arguments = {"overwrite contents", ">", "/file1"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "overwrite contents";
    String actualContents = this.mfs.file1.getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to an existing file, appending
  // to its contents
  @Test
  public void executeTest4() {
    String[] arguments = {" appending to previous content", ">>", "/file1"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents =
        "Hello World! This is file1. appending to previous content";
    String actualContents = this.mfs.file1.getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a valid path but not an
  // existing file using ">" in relative path
  @Test
  public void executeTest5() {
    String[] arguments = {"new contents", ">", "file1"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "new contents";
    String actualContents =
        this.mfs.mfs.getCurrentDir().getAtIndex(0).getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a valid path but not an
  // existing file using ">>" in absolute path
  @Test
  public void executeTest6() {
    String[] arguments = {"new contents", ">>", "/newfile"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "new contents";
    String actualContents = this.mfs.mfs.getRoot().getAtIndex(3).getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a valid absolute path but not
  // an existing file using ">>" in relative path containing .. and .
  @Test
  public void executeTest7() {
    String[] arguments = {"new contents", ">>", "/dir1/dir11/.././newFile"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "new contents";
    String actualContents = this.mfs.dir1.getAtIndex(2).getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a valid absolute path
  // containing .. and . to an existing file and using ">" to overwrite the file
  @Test
  public void executeTest8() {
    String[] arguments =
        {"overwriting again", ">", "/dir1/dir11/../.././file1"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "overwriting again";
    String actualContents = this.mfs.file1.getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a valid relative path
  // containing .. and . to an existing file and using ">>" to append to that
  // file
  @Test
  public void executeTest9() {
    String[] arguments = {" appending again", ">>", "../../dir1/file2"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "Hi! This is file2. appending again";
    String actualContents = this.mfs.file2.getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a valid relative path
  // containing .. and . to an existing file and using ">" to overwrite the
  // contents
  @Test
  public void executeTest10() {
    String[] arguments = {"overwriting file1", ">", "../../file1"};
    test = new Echo(arguments);
    String output = test.execute();
    String expectedContents = "overwriting file1";
    String actualContents = this.mfs.file1.getContent();
    assertEquals(expectedContents, actualContents);
    assertEquals(output, null);
  }

  // Test the execute method with redirection to a invalid path
  @Test
  public void executeTest11() {
    String[] arguments = {"random content", ">", "this/path/is/invalid"};
    test = new Echo(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getCurrentDir().sizeOfContents(), 0);
    assertEquals(output, null);
  }

}
