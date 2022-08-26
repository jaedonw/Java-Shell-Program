package test;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import a2.Command;
import a2.Directory;
import a2.ErrorChecker;
import a2.FileSystem;
import driver.JShell;

public class ErrorCheckerTest {

  ErrorChecker errorChecker;
  FileSystem fileSystem;

  @Before
  public void setUp() {
    errorChecker = new ErrorChecker();

    // Setting up the file system that method checkIfPathExists() depends on. It
    // is assumed that these dependencies are working.
    fileSystem = FileSystem.getFileSystemInstance();
    Directory root = fileSystem.getRoot();
    Directory dir1 = new Directory("dir1", "/dir1", root);
    Directory dir2 = new Directory("dir2", "/dir1/dir2", dir1);
    Directory dir3 = new Directory("dir3", "/dir1/dir2/dir3", dir2);

    dir1.addContent(dir2);
    dir2.addContent(dir3);
    root.addContent(dir1);

    JShell.setFileSystem(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);
  }

  // test case: when the command has the right number of arguments
  @Test
  public void checkNumArgumentsTest1() {
    String[] arguments = {"1", "2", "3"};
    Command command = new MockCommand(arguments);
    assertTrue(errorChecker.checkNumArguments(command, 3));
  }

  // test case: when the command has the wrong number of arguments
  @Test
  public void checkNumArgumentsTest2() {
    String[] arguments = {"1", "2", "3"};
    Command command = new MockCommand(arguments);
    assertFalse(errorChecker.checkNumArguments(command, 2));
  }

  // test case: when the command is missing required arguments
  @Test
  public void checkMissingArgumentsTest1() {
    String[] arguments = {"1", "2", "3"};
    Command command = new MockCommand(arguments);
    assertFalse(errorChecker.checkMissingArguments(command, 5));
  }

  // test case: when the command is not missing required arguments
  @Test
  public void checkMissingArgumentsTest2() {
    String[] arguments = {"1", "2", "3"};
    Command command = new MockCommand(arguments);
    assertTrue(errorChecker.checkMissingArguments(command, 3));
  }

  // test case: when the command has too many arguments
  @Test
  public void checkExcessArgumentsTest1() {
    String[] arguments = {"1", "2", "3"};
    Command command = new MockCommand(arguments);
    assertFalse(errorChecker.checkExcessArguments(command, 2));
  }

  // test case: when the command does not have too many arguments
  @Test
  public void checkExcessArgumentsTest2() {
    String[] arguments = {"1", "2", "3"};
    Command command = new MockCommand(arguments);
    assertTrue(errorChecker.checkExcessArguments(command, 4));
  }

  // test case: a valid file name
  @Test
  public void checkValidFileNameTest1() {
    assertTrue(errorChecker.checkValidFileName("file"));
  }

  // test case: an almost valid file name
  @Test
  public void checkValidFileNameTest2() {
    assertFalse(errorChecker.checkValidFileName("file$"));
  }

  // test case: an very invalid file name
  @Test
  public void checkValidFileNameTest3() {
    assertFalse(errorChecker.checkValidFileName("!new@file$>>"));
  }

  // NOTE: Dependency injection cannot be applied when testing method
  // checkIfPathExists() due to its use of static methods. Therefore, the
  // following test cases assume that the implementations of those static
  // methods are working.

  @Test
  public void checkIfPathExistsTest1() {
    assertTrue(errorChecker.checkIfPathExists("/"));
  }

  @Test
  public void checkIfPathExistsTest2() {
    assertTrue(errorChecker.checkIfPathExists("dir1"));
  }

  @Test
  public void checkIfPathExistsTest3() {
    assertTrue(errorChecker.checkIfPathExists("/dir1/dir2"));
  }

  @Test
  public void checkIfPathExistsTest4() {
    assertTrue(errorChecker.checkIfPathExists("dir1/dir2/dir3"));
  }

  @Test
  public void checkIfPathExistsTest5() {
    assertFalse(errorChecker.checkIfPathExists("hello"));
  }

  @Test
  public void checkIfPathExistsTest6() {
    assertFalse(errorChecker.checkIfPathExists("/dir1/hello"));
  }

  @Test
  public void checkIfPathExistsTest7() {
    assertFalse(errorChecker.checkIfPathExists("dir1/dir2/hello"));
  }

  @Test
  public void checkIfPathExistsTest8() {
    assertFalse(errorChecker.checkIfPathExists("/dir1/dir2/dir3/hello"));
  }


  // NOTE: Dependency injection cannot be applied when testing method
  // checkIfPathCanBeMade() due to its use of static methods. Therefore, the
  // following test cases assume that the implementations of those static
  // methods are working.

  @Test
  public void checkIfPathCanBeMadeTest1() {
    assertTrue(errorChecker.checkIfPathCanBeMade("/dir4"));
  }

  @Test
  public void checkIfPathCanBeMadeTest2() {
    assertTrue(errorChecker.checkIfPathCanBeMade("dir1/dir5"));
  }

  @Test
  public void checkIfPathCanBeMadeTest3() {
    assertTrue(errorChecker.checkIfPathCanBeMade("/dir1/dir2/dir6"));
  }

  @Test
  public void checkIfPathCanBeMadeTest4() {
    assertTrue(errorChecker.checkIfPathCanBeMade("dir1/dir2/dir3/dir7"));
  }

  @Test
  public void checkIfPathCanBeMadeTest5() {
    assertFalse(errorChecker.checkIfPathCanBeMade("/dir8/dir9"));
  }

  @Test
  public void checkIfPathCanBeMadeTest6() {
    assertFalse(errorChecker.checkIfPathCanBeMade("dir1/dir8/dir9"));
  }

  @Test
  public void checkIfPathCanBeMadeTest7() {
    assertFalse(errorChecker.checkIfPathCanBeMade("dir1/dir2/dir8/dir9"));
  }

  @Test
  public void checkIfPathCanBeMadeTest8() {
    assertFalse(errorChecker.checkIfPathCanBeMade("dir1/dir2/dir3/dir8/dir9"));
  }

}
