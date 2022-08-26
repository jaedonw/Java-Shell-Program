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
import a2.Curl;
import a2.Directory;
import a2.File;
import a2.FileSystem;
import driver.JShell;

public class CurlTest {

  Curl c;
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
    c = new Curl(arguments);
    assertTrue(c.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    c = new Curl(arguments);
    assertTrue(c.getArguments().equals(arguments));
  }

  // testing execution of a .txt url
  @Test
  public void executeTest1() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String url =
        "http://www.cs.utoronto.ca/~trebla/CSCB09-2020-Summer/l02/comm-input-1.txt";
    String arguments[] = {url};
    String expected =
        "baklava\n" + "calamari\n" + "lasagna\n" + "steak\n" + "wine\n";
    Curl c1 = new Curl(arguments);
    assertEquals(null, c1.execute());

    File check = dir1.getAtIndex(0);
    assertTrue(check.getContent().equals(expected));
  }

  // testing execution of a nonexistent url
  @Test
  public void executeTest2() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String url = "a";
    String arguments[] = {url};

    Curl c1 = new Curl(arguments);
    assertEquals("invalid URL", c1.execute());
  }

  // testing execution of an unsupported url type
  @Test
  public void executeTest3() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String url = "http://www.google.ca";
    String arguments[] = {url};

    Curl c1 = new Curl(arguments);
    assertEquals("incorrect filetype", c1.execute());
  }
  //
}
