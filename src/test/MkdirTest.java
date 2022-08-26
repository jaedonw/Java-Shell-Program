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
import a2.Mkdir;

public class MkdirTest {

  Mkdir test;
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
   * Mkdir class since it uses static methods from other classes. The test cases
   * below assume that the other methods it uses are completely functional
   */

  // Test the mkdir constructor with no arguments provided
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    test = new Mkdir(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the mkdir constructor with two arguments provided
  @Test
  public void constructorTest2() {
    String arguments[] = {"dir1", "dir2"};
    test = new Mkdir(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Test the mkdir constructor with arguments that include .. and .
  @Test
  public void constructorTest3() {
    String arguments[] = {"../first", "./second"};
    test = new Mkdir(arguments);
    assertEquals(test.getArguments()[0], "/dir1/first");
    assertEquals(test.getArguments()[1], "/dir1/dir11/second");
  }

  /*
   * NOTE: Dependency injection cannot be applied for the constructor inside the
   * Mkdir class since it uses static methods from other classes. The test cases
   * below assume that the other methods it uses are completely functional
   */

  // Test the execute method with creating a directory and the path is relative
  @Test
  public void executeTest1() {
    String arguments[] = {"dir111"};
    test = new Mkdir(arguments);
    String output = test.execute();
    File test1 = this.mfs.mfs.getCurrentDir().getAtIndex(0);
    assertTrue(test1 instanceof Directory);
    assertEquals(test1.getFileName(), "dir111");
    assertEquals(output, null);
  }

  // Test the execute method with creating a directory and path is absolute
  @Test
  public void executeTest2() {
    String arguments[] = {"/dir1/dir11/dir111"};
    test = new Mkdir(arguments);
    String output = test.execute();
    File test1 = this.mfs.mfs.getCurrentDir().getAtIndex(0);
    assertTrue(test1 instanceof Directory);
    assertEquals(test1.getFileName(), "dir111");
    assertEquals(output, null);
  }

  // Test the execute method with creating a directory and path is absolute and
  // contains .. and .
  @Test
  public void executeTest3() {
    String arguments[] = {"/dir1/dir11/../.././dir2/newDir"};
    test = new Mkdir(arguments);
    String output = test.execute();
    File test1 = this.mfs.dir2.getAtIndex(0);
    assertTrue(test1 instanceof Directory);
    assertEquals(test1.getFileName(), "newDir");
    assertEquals(output, null);
  }

  // Test the execute method with creating a directory and path is relative and
  // contains .. and .
  @Test
  public void executeTest4() {
    String arguments[] = {".././dir3"};
    test = new Mkdir(arguments);
    String output = test.execute();
    File test1 = this.mfs.dir1.getAtIndex(2);
    assertTrue(test1 instanceof Directory);
    assertEquals(test1.getFileName(), "dir3");
    assertEquals(output, null);
  }

  // Test the execute method with creation of multiple directories (more than 1
  // arguments) and both are relative to current
  @Test
  public void executeTest5() {
    String arguments[] = {"dir1", "dir2"};
    test = new Mkdir(arguments);
    String output = test.execute();
    File test1 = this.mfs.mfs.getCurrentDir().getAtIndex(0);
    File test2 = this.mfs.mfs.getCurrentDir().getAtIndex(1);
    assertTrue(test1 instanceof Directory);
    assertTrue(test2 instanceof Directory);
    assertEquals(test1.getFileName(), "dir1");
    assertEquals(test2.getFileName(), "dir2");
    assertEquals(output, null);
  }

  // Test the execute method with creation of multiple directories (more than 1
  // argument) some are relative and some are absolute
  @Test
  public void executeTest6() {
    String arguments[] = {"dir1", "/dir3"};
    test = new Mkdir(arguments);
    String output = test.execute();
    File test1 = this.mfs.mfs.getCurrentDir().getAtIndex(0);
    File test2 = this.mfs.mfs.getRoot().getAtIndex(3);
    assertTrue(test1 instanceof Directory);
    assertTrue(test2 instanceof Directory);
    assertEquals(test1.getFileName(), "dir1");
    assertEquals(test2.getFileName(), "dir3");
    assertEquals(output, null);
  }

  // Test the execute method with no inputs
  @Test
  public void executeTest7() {
    String arguments[] = {};
    test = new Mkdir(arguments);
    String output = test.execute();
    assertEquals(output, "mkdir: requires atleast one argument");
  }

  // Test the execute method with a non reachable path
  @Test
  public void executeTest8() {
    String[] arguments = {"/this/path/does/not/exist"};
    test = new Mkdir(arguments);
    String output = test.execute();
    assertEquals(output, null);
  }

  // Test the execute method with paths that contain "//"
  @Test
  public void executeTest9() {
    String[] arguments = {"/dir1//dir11/dir3", "////dir1/dir5"};
    test = new Mkdir(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getCurrentDir().sizeOfContents(), 0);
    assertEquals(output, null);
  }

  // Test the execute method with invalid name
  @Test
  public void executeTest10() {
    String[] arguments = {"!@##@!"};
    test = new Mkdir(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getCurrentDir().sizeOfContents(), 0);
    assertEquals(output, null);
  }

}
