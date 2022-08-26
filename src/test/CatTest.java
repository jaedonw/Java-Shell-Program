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
import a2.Cat;
import a2.Directory;
import a2.Echo;
import a2.File;
import a2.FileSystem;
import driver.JShell;

public class CatTest {

  FileSystem fs;
  Directory dir1;
  File f1, f2;
  Cat c;

  @Before
  public void setUp() throws Exception {
    fs = FileSystem.getFileSystemInstance();
    dir1 = new Directory("dir1", "/dir1", fs.getRoot());
    fs.getRoot().addContent(dir1);
    f1 = new File("file1", "this is f1", "/dir1/f1", dir1);
    f2 = new File("file2", "this is f2", "/dir1/f2", fs.getRoot());
    dir1.addContent(f1);
    dir1.addContent(f2);
    JShell.setFileSystem(fs);
  }

  // constructor with no arguments
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    c = new Cat(arguments);
    assertTrue(c.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    c = new Cat(arguments);
    assertTrue(c.getArguments().equals(arguments));
  }

  // execute with no arguments
  @Test
  public void executeTest1() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String arguments[] = {};
    Cat c1 = new Cat(arguments);
    assertEquals(c1.execute(), null);
  }

  // execute with nonexistent file
  @Test
  public void executeTest2() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String arguments[] = {"notafile"};
    Cat c1 = new Cat(arguments);
    assertEquals(c1.execute(), "");
  }

  // execute with one existing file
  @Test
  public void executeTest3() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String arg1[] = {"thisisf1", ">", "f1"};
    Echo e = new Echo(arg1);
    e.execute();
    String arguments[] = {"f1"};
    Cat c1 = new Cat(arguments);
    assertEquals(c1.execute(), "thisisf1\n\n\n");
  }

  // execute with more than one existing file
  @Test
  public void executeTest4() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String arg1[] = {"thisisf1", ">", "f1"};
    String arg2[] = {"thisisf2", ">", "f2"};
    Echo e = new Echo(arg1);
    e.execute();
    e = new Echo(arg2);
    e.execute();
    String arguments[] = {"f1", "f2"};
    Cat c1 = new Cat(arguments);
    assertEquals(c1.execute(), "thisisf1\n\n\nthisisf2\n\n\n");
  }
}
