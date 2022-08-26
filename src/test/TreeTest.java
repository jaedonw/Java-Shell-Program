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
import a2.Tree;
import a2.File;
import a2.Directory;

public class TreeTest {

  Tree test;
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
   * Tree class since it uses static methods from other classes. The test cases
   * below assume that the other methods it uses are completely functional
   */

  // Tests the tree constructor with empty input
  @Test
  public void constructorTest1() {
    String[] arguments = {};
    test = new Tree(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Tests the tree constructor with two inputs
  @Test
  public void constructorTest2() {
    String[] arguments = {">", "/dog"};
    test = new Tree(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Tests the tree constructor with two inputs but second is contains ..
  // constructor in tree deals with paths that contains .. or .
  @Test
  public void constructorTest3() {
    String[] arguments = {">", "/dog/.."};
    test = new Tree(arguments);
    assertTrue(test.getArguments()[0].equals(">"));
    assertTrue(test.getArguments()[1].equals("/"));
  }

  /*
   * NOTE: Dependency injection cannot be applied for the execute method inside
   * the Tree class since it uses static methods from other classes. The test
   * cases below assume that the other methods it uses are completely functional
   */

  // Test the execute method on an empty input, prints out the
  // tree which would look like what the expected has
  @Test
  public void executeTest1() {
    String[] arguments = {};
    test = new Tree(arguments);
    String expected = "/\n  file1\n  dir2\n  dir1\n    dir11\n    file2\n";
    assertEquals(expected, test.execute());
  }

  // Test the execute method given one arguments which would result in an error
  // and no output
  @Test
  public void executeTest2() {
    String[] arguments = {"hello"};
    test = new Tree(arguments);
    assertEquals(null, test.execute());
  }

  // Test the execute method given more than one argument which would result
  // in an error and no output
  @Test
  public void executeTest3() {
    String[] arguments = {"first", "second", "third", "forth", "fifth"};
    test = new Tree(arguments);
    assertEquals(null, test.execute());
  }

  // Test the execute method again adding a new directory inside
  @Test
  public void executeTest4() {
    // adding newDir in dir1
    Directory newDir = new Directory("newDir", "/dir1/newDir", this.mfs.dir1);
    this.mfs.dir1.addContent(newDir);
    String[] arguments = {};
    test = new Tree(arguments);
    String output = test.execute();
    String expected =
        "/\n  file1\n  dir2\n  dir1\n    newDir\n    dir11\n    file2\n";
    assertTrue(output.equals(expected));
  }

  // Test the execute method with a directory that contains multiple
  // subdirectories
  @Test
  public void executeTest5() {
    Directory dir111 =
        new Directory("dir111", "/dir1/dir11/dir111", this.mfs.dir11);
    this.mfs.dir11.addContent(dir111);
    Directory dir1111 =
        new Directory("dir1111", "/dir1/dir11/dir111/dir1111", dir111);
    dir111.addContent(dir1111);
    File file123 =
        new File("file123", "/dir1/dir11/dir111/dir1111/file123", dir1111);
    dir1111.addContent(file123);
    String[] arguments = {};
    test = new Tree(arguments);
    String output = test.execute();
    String expected =
        "/\n  file1\n  dir2\n  dir1\n    dir11\n      dir111\n        dir1111\n"
            + "          file123\n    file2\n";
    assertEquals(output, expected);
  }

  // Test the execute method multiple directories and its subdirectories contain
  // files/other directories
  @Test
  public void executeTest6() {
    Directory addDir1 =
        new Directory("addDir1", "/dir1/addDir1", this.mfs.dir1);
    this.mfs.dir1.addContent(addDir1);
    File addFile1 = new File("addFile1", "/dir1/addDir1/addFile1", addDir1);
    File addFile2 = new File("addFile2", "/dir1/addDir1/addFile2", addDir1);
    File addFile3 = new File("addFile3", "/dir1/addDir1/addFile3", addDir1);
    addDir1.addContent(addFile1);
    addDir1.addContent(addFile2);
    addDir1.addContent(addFile3);
    File addFile4 = new File("addFile4", "/dir2/addFile4", this.mfs.dir2);
    File addFile5 = new File("addFile5", "/dir2/addFile5", this.mfs.dir2);
    File addFile6 = new File("addFile6", "/dir2/addFile6", this.mfs.dir2);
    this.mfs.dir2.addContent(addFile4);
    this.mfs.dir2.addContent(addFile5);
    this.mfs.dir2.addContent(addFile6);
    String[] arguments = {};
    test = new Tree(arguments);
    String output = test.execute();
    String expected =
        "/\n  file1\n  dir2\n    addFile6\n    addFile5\n    addFile4\n  dir1\n"
            + "    addDir1\n      addFile3\n      addFile2\n      addFile1\n"
            + "    dir11\n    file2\n";
    assertEquals(output, expected);
  }

  // Test the execute method with file redirection with valid input and creating
  // a file since it does not exist
  @Test
  public void executeTest7() {
    String[] arguments = {">", "outputFile"};
    test = new Tree(arguments);
    String output = test.execute();
    File outputFile = this.mfs.dir11.getAtIndex(0);
    assertEquals(outputFile.getContent(),
        "/\n  file1\n  dir2\n  dir1\n    dir11\n    file2\n");
    assertEquals(output, null);
  }

  // Test the execute method with file redirection with valid input and
  // overwriting an existing file
  @Test
  public void executeTest8() {
    File file1 = new File("file1", "existing contents", "/dir1/dir11/file1",
        this.mfs.dir11);
    this.mfs.dir11.addContent(file1);
    String[] arguments = {">", "file1"};
    test = new Tree(arguments);
    String output = test.execute();
    assertEquals(file1.getContent(),
        "/\n  file1\n  dir2\n  dir1\n    dir11\n      file1\n    file2\n");
    assertEquals(output, null);
  }

  // Test the execute method with file redirection with valid input and
  // appending to an existing file
  @Test
  public void executeTest9() {
    File file1 = new File("file1", "existing contents", "/dir1/dir11/file1",
        this.mfs.dir11);
    this.mfs.dir11.addContent(file1);
    String[] arguments = {">>", "file1"};
    test = new Tree(arguments);
    String output = test.execute();
    assertEquals(file1.getContent(),
        "existing contents/\n  file1\n  dir2\n  dir1\n    dir11\n      file1\n"
            + "    file2\n");
    assertEquals(output, null);
  }

  // Test the execute method with file redirection with valid input and
  // overwriting a file using absolute path
  @Test
  public void executeTest10() {
    String[] arguments = {">", "/file1"};
    test = new Tree(arguments);
    String output = test.execute();
    assertEquals(this.mfs.file1.getContent(),
        "/\n  file1\n  dir2\n  dir1\n    dir11\n    file2\n");
    assertEquals(output, null);
  }

  // Test the execute method with file redirection with valid input and
  // append a file using relative path that contains .. and .
  @Test
  public void executeTest11() {
    String[] arguments = {">>", "../../file1"};
    test = new Tree(arguments);
    String output = test.execute();
    assertEquals(this.mfs.file1.getContent(),
        "Hello World! This is file1./\n  file1\n  dir2\n  dir1\n    dir11\n"
            + "    file2\n");
    assertEquals(output, null);
  }

  // Test the execute method with file redirection with invalid file name
  @Test
  public void executeTest12() {
    String[] arguments = {">", "!@#$"};
    test = new Tree(arguments);
    String output = test.execute();
    assertEquals(output, null);
  }

}
