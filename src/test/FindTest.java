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
import a2.Directory;
import a2.File;
import a2.Find;

public class FindTest {

  Find test;
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
   * Find class since it uses static methods from other classes. The test cases
   * below assume that the other methods it uses are completely functional
   */

  // Tests the constructor with no arguments
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    test = new Find(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the constructor with five arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"/", "-type", "d", "-name", "bob"};
    test = new Find(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the constructor with seven arguments
  @Test
  public void constructorTest3() {
    String arguments[] = {"/", "-type", "d", "-name", "bob", ">", "johny"};
    test = new Find(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the constructor for redirection for paths that contains .. and . in
  // relative
  @Test
  public void constructorTest4() {
    String arguments[] =
        {"/", "-type", "d", "-name", "john", ">", "../path1/./path2"};
    test = new Find(arguments);
    assertEquals(test.getArguments()[6], "/dir1/path1/path2");
  }

  // Test the constructor for redirection for paths that contains .. and . in
  // absolute
  @Test
  public void constructorTest5() {
    String arguments[] =
        {"/", "-type", "d", "-name", "john", ">", "/path1/path2/skipped/.."};
    test = new Find(arguments);
    assertEquals(test.getArguments()[6], "/path1/path2");
  }
  /*
   * NOTE: Dependency injection cannot be applied for the execute method inside
   * the Find class since it uses static methods from other classes. The test
   * cases below assume that the other methods it uses are completely functional
   */

  // Test the execute method with an invalid arguments
  @Test
  public void executeTest1() {
    String arguments[] = {"/", "-type", "d", "invalid", "file"};
    Find test1 = new Find(arguments);
    String output = test1.execute();
    String expected =
        "find: Usage `find [path ...] -type [f|d] -name expression`";
    assertTrue(output.equals(expected));
  }

  // Test the execute method with valid arguments and searched the path
  // searching for a file
  @Test
  public void executeTest2() {
    String arguments[] = {"/", "-type", "f", "-name", "\"file1\""};
    test = new Find(arguments);
    String output = test.execute();
    String expected = "Found file in /file1\n";
    assertTrue(output.equals(expected));
  }

  // Test the execute method with valid arguments and search the path but
  // the type search option is set to d while there exist a file with that name
  // but not a directory of that
  @Test
  public void executeTest3() {
    String arguments[] = {"/", "-type", "d", "-name", "\"file1\""};
    test = new Find(arguments);
    String output = test.execute();
    String expected = "";
    assertTrue(output.equals(expected));
  }

  // Test the execute method with valid arguments and search multiple paths
  // searching for a file
  @Test
  public void executeTest4() {
    String arguments[] =
        {"/", "/dir1", "/dir2", "-type", "f", "-name", "\"file1\""};
    test = new Find(arguments);
    String output = test.execute();
    String expected = "Found file in /file1\n";
    assertTrue(output.equals(expected));
  }

  // Test the execute method by searching two separate directories that contains
  // the same file name
  @Test
  public void executeTest5() {
    File file2 = new File("file2", "/dir2/file2", this.mfs.dir2);
    this.mfs.dir2.addContent(file2);
    String arguments[] = {"/dir2", "/dir1", "-type", "f", "-name", "\"file2\""};
    test = new Find(arguments);
    String output = test.execute();
    String expected = "Found file in /dir2/file2\nFound file in /dir1/file2\n";
    assertEquals(output, expected);
  }

  // Test the execute method by searching starting from the root for a file
  // called "this" but there is also a directory called "this"
  @Test
  public void executeTest6() {
    File this1 = new File("this", "/dir2/this", this.mfs.dir2);
    this.mfs.dir2.addContent(this1);
    Directory this2 = new Directory("this", "/this", this.mfs.mfs.getRoot());
    this.mfs.mfs.getRoot().addContent(this2);
    String arguments[] = {"/", "-type", "f", "-name", "\"this\""};
    test = new Find(arguments);
    String output = test.execute();
    String expected = "Found file in /dir2/this\n";
    assertEquals(output, expected);
  }

  // Test the execute method by searching starting from the root for a directory
  // called "this" but there also exist another file called "this"
  @Test
  public void executeTest7() {
    File this1 = new File("this", "/dir2/this", this.mfs.dir2);
    this.mfs.dir2.addContent(this1);
    Directory this2 = new Directory("this", "/this", this.mfs.mfs.getRoot());
    this.mfs.mfs.getRoot().addContent(this2);
    String arguments[] = {"/", "-type", "d", "-name", "\"this\""};
    test = new Find(arguments);
    String output = test.execute();
    String expected = "Found directory in /this\n";
    assertEquals(output, expected);
  }

  // Test the execute method with invalid path
  @Test
  public void executeTest8() {
    String arguments[] =
        {"/this/path/does/not/exist", "-type", "f", "-name", "\"file\""};
    test = new Find(arguments);
    String output = test.execute();
    assertEquals(output, "");
  }

  // Test the execute method with mixture of valid and invalids paths
  @Test
  public void executeTest9() {
    String arguments[] =
        {"/this/path/does/not/exist", "/", "-type", "f", "-name", "\"file1\""};
    test = new Find(arguments);
    String output = test.execute();
    assertEquals(output, "Found file in /file1\n");
  }

  // Test the execute method with mixture of valid and invalid paths, found file
  // in valid paths
  @Test
  public void executeTest10() {
    String arguments[] = {"/this/path/does/not/exist", "/dir1", "/dir2",
        "-type", "f", "-name", "\"file2\""};
    File file2 = new File("file2", "/dir2/file2", this.mfs.dir2);
    this.mfs.dir2.addContent(file2);
    test = new Find(arguments);
    String output = test.execute();
    String expected = "Found file in /dir1/file2\nFound file in /dir2/file2\n";
    assertEquals(output, expected);
  }

}
