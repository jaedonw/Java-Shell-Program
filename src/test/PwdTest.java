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
import a2.Pwd;
import driver.JShell;

public class PwdTest {

  Pwd p;
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
    p = new Pwd(arguments);
    assertTrue(p.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    p = new Pwd(arguments);
    assertTrue(p.getArguments().equals(arguments));
  }

  // proper pwd execution
  @Test
  public void executeTest1() {
    String arguments[] = {};
    Pwd p1 = new Pwd(arguments);
    assertEquals(p1.execute(), "/");
  }

  // pwd execution with extra arguments
  @Test
  public void executeTest2() {
    String arguments[] = {"extraargument"};
    Pwd p1 = new Pwd(arguments);
    assertEquals(p1.execute(), null);
  }
}
