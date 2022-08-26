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
import a2.Mv;

public class MvTest {

  Mv test;
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
   * Mv class since it uses static methods from other classes. The test cases
   * below assume that the other methods it uses are completely functional
   */

  // Testing the constructor with two arguments passed in
  @Test
  public void constructorTest1() {
    String[] arguments = {"file1", "file2"};
    test = new Mv(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Testing the constructor with more than two arguments passed in
  @Test
  public void constructorTest2() {
    String[] arguments = {"file1", "file2", "file3", "file4", "file5"};
    test = new Mv(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Testing the constructor with no arguments passed in
  @Test
  public void constructorTest3() {
    String[] arguments = {};
    test = new Mv(arguments);
    assertTrue(test.getArguments().equals(arguments));
  }

  // Testing the constructor with argument passed in containing . and .. in
  // relative path
  @Test
  public void constructorTest4() {
    String[] arguments = {"../../dir2", ".."};
    test = new Mv(arguments);
    String expected1 = "/dir2";
    String expected2 = "/dir1";
    assertEquals(test.getArguments()[0], expected1);
    assertEquals(test.getArguments()[1], expected2);
  }

  // Testing the constructor with arguments passed in containing . and .. in
  // absolute path
  @Test
  public void constructorTest5() {
    String[] arguments = {"/dir1/dir11/../", "/dir2/.."};
    test = new Mv(arguments);
    String expected1 = "/dir1";
    String expected2 = "/";
    assertEquals(test.getArguments()[0], expected1);
    assertEquals(test.getArguments()[1], expected2);
  }

  /*
   * NOTE: Dependency injection cannot be applied for the execute method inside
   * the Mv class since it uses static methods from other classes. The test
   * cases below assume that the other methods it uses are completely functional
   */

  // Moving a valid path to a file to a valid path to a directory using
  // absolute path
  @Test
  public void executeTest1() {
    String arguments[] = {"/file1", "/dir1/dir11"};
    test = new Mv(arguments);
    String output = test.execute();
    File testFile = this.mfs.dir11.getAtIndex(0);
    assertEquals(testFile.getPathname(), "/dir1/dir11/file1");
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 2);
    assertEquals(output, null);
  }

  // Moving a valid path to a file to a valid path to a file using absolute path
  // should result in file2 being overwritten by file1 contents
  @Test
  public void executeTest2() {
    String arguments[] = {"/file1", "/dir1/file2"};
    test = new Mv(arguments);
    String output = test.execute();
    File testFile = this.mfs.dir1.getAtIndex(1);
    String expected = "Hello World! This is file1.";
    assertEquals(output, null);
    assertEquals(testFile.getContent(), expected);
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 2);
  }

  // Moving a valid path directory into a valid path file using absolute path
  // this should result in an error an nothing moved
  @Test
  public void executeTest3() {
    String arguments[] = {"/dir1", "/file1"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(output, "usage: mv OLDPATH NEWPATH");
  }

  // Moving a valid path directory into a valid path directory using absolute
  // path this should result in a move of the directory
  @Test
  public void executeTest4() {
    String arguments[] = {"/dir1", "/dir2"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 2);
    assertEquals(this.mfs.dir2.sizeOfContents(), 1);
    assertEquals(output, null);
  }

  // Moving a valid path file to a directory and in this directory it contains
  // the file you want to move which result in overwriting that file inside the
  // directory using absolute path
  @Test
  public void executeTest5() {
    File file1 =
        new File("file1", "junk contents", "/dir1/file1", this.mfs.dir1);
    this.mfs.dir1.addContent(file1);
    String arguments[] = {"/file1", "/dir1"};
    test = new Mv(arguments);
    String output = test.execute();
    File testFound = this.mfs.dir1.getAtIndex(2);
    assertEquals(testFound.getContent(), "Hello World! This is file1.");
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 2);
    assertEquals(output, null);
  }

  // Moving a valid path to a directory to a valid path to a Directory in which
  // the directory name is the same as the file name so it should result in
  // overwriting that file with the directory using absolute path
  @Test
  public void executeTest6() {
    Directory dog1 = new Directory("dog", "/dog", this.mfs.mfs.getRoot());
    this.mfs.mfs.getRoot().addContent(dog1);
    Directory dog2 = new Directory("dog", "/dir1/dog", this.mfs.dir1);
    this.mfs.dir1.addContent(dog2);
    File dog3 = new File("subdog", "/dog/subdog", dog1);
    dog1.addContent(dog3);
    String arguments[] = {"/dog", "/dir1"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(this.mfs.dir1.sizeOfContents(), 3);
    assertEquals(dog1.sizeOfContents(), 1);
    assertEquals(output, null);
  }

  // Renaming a file, old path exist but new path does not exist but is valid
  // using absolute path
  @Test
  public void executeTest7() {
    String[] arguments = {"/file1", "/renamed"};
    test = new Mv(arguments);
    String output = test.execute();
    File foundFile = this.mfs.mfs.getRoot().getAtIndex(2);
    assertEquals(foundFile.getContent(), "Hello World! This is file1.");
    assertEquals(output, null);
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
  }

  // Renaming a directory, old path exist but new path does not exist but is
  // valid using absolute path
  @Test
  public void executeTest8() {
    String[] arguments = {"/dir1/dir11", "/renamedDir11"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 4);
    assertEquals(this.mfs.dir1.sizeOfContents(), 1);
    assertEquals(output, null);
  }

  // Trying to move a file that doesn't exist into a file that does exist using
  // absolute path
  @Test
  public void executeTest9() {
    String[] arguments = {"/does/not/exist", "/dir1"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(this.mfs.dir1.sizeOfContents(), 2);
    assertEquals(output, null);
  }

  // Trying to move a file that doesn't exist into a file that also doesn't
  // exist using absolute path
  @Test
  public void executeTest10() {
    String[] arguments = {"/does/not/exist", "/does/not/exist"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(this.mfs.dir1.sizeOfContents(), 2);
    assertEquals(output, null);
  }

  // Trying to move a parent directory into one of it's child sub directories
  @Test
  public void executeTest11() {
    String[] arguments = {"/dir1", "/dir1/dir11"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(this.mfs.dir1.sizeOfContents(), 2);
    assertEquals(output, null);
  }

  // Trying to move the root directory into a sub directory
  @Test
  public void executeTest12() {
    String[] arguments = {"/", "/dir1"};
    test = new Mv(arguments);
    String output = test.execute();
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(this.mfs.dir1.sizeOfContents(), 2);
    assertEquals(output, null);
  }

  // overwriting an existing file with a file using relative path with .. and .
  @Test
  public void executeTest13() {
    String[] arguments = {"../file2", "../../file1"};
    test = new Mv(arguments);
    String output = test.execute();
    File foundFile = this.mfs.mfs.getRoot().getAtIndex(2);
    assertEquals(foundFile.getContent(), "Hi! This is file2.");
    assertEquals(output, null);
    assertEquals(this.mfs.mfs.getRoot().sizeOfContents(), 3);
    assertEquals(this.mfs.dir1.sizeOfContents(), 1);
  }

}
