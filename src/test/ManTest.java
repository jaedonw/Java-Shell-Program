// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import a2.Directory;
import a2.FileSystem;
import a2.Man;
import driver.JShell;

public class ManTest {

  Man m;
  FileSystem fs;
  Directory dir1;

  @Before
  public void setUp() throws Exception {
    fs = FileSystem.getFileSystemInstance();
    dir1 = new Directory("dir1", "/dir1", fs.getRoot());
    fs.getRoot().addContent(dir1);

    JShell.setFileSystem(fs);
  }

  // constructor with no arguments
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    m = new Man(arguments);
    assertTrue(m.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    m = new Man(arguments);
    assertTrue(m.getArguments().equals(arguments));
  }

  // execution with no arguments
  @Test
  public void executeTest1() {
    String arguments[] = {};
    Man m1 = new Man(arguments);
    assertEquals(m1.execute(), null);
  }

  // execution with 1 proper argument
  @Test
  public void executeTest2() {
    String arguments[] = {"pwd"};
    Man m1 = new Man(arguments);
    String expected =
        "NAME: pwd\n" + "DESCRIPTION: Prints the current working directory.\n";
    assertEquals(m1.execute(), expected);
  }

  // execution with too many arguments
  @Test
  public void executeTest3() {
    String arguments[] = {"pwd", "echo"};
    Man m1 = new Man(arguments);
    assertEquals(m1.execute(), null);
  }

  // execution with an invalid argument
  @Test
  public void executeTest4() {
    String arguments[] = {"notacommand"};
    Man m1 = new Man(arguments);
    assertEquals(m1.execute(), null);
  }


}
