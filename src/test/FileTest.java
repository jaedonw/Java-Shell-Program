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
import a2.FileSystem;
import driver.JShell;

public class FileTest {

  FileSystem fileSystem;
  Directory dir1;
  Directory dir2;
  Directory dir3;
  File file1;
  File file2;
  File file3;

  // Setting up the file system that method getFile() depends on. It
  // is assumed that these dependencies are working.
  @Before
  public void setUp() {
    fileSystem = FileSystem.getFileSystemInstance();
    dir1 = new Directory("dir1", "/dir1", fileSystem.getRoot());
    file1 = new File("file1", "/file1", fileSystem.getRoot());
    dir2 = new Directory("dir2", "/dir1/dir2", dir1);
    file2 = new File("file2", "this is file2", "/dir1/file2", dir1);
    dir3 = new Directory("dir3", "/dir1/dir2/dir3", dir2);
    file3 = new File("file3", "/dir1/dir2/file3", dir2);

    fileSystem.getRoot().addContent(dir1);
    fileSystem.getRoot().addContent(file1);
    dir1.addContent(dir2);
    dir1.addContent(file2);
    dir2.addContent(dir3);
    dir2.addContent(file3);

    JShell.setFileSystem(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);
  }

  // test case: constructing a File object with no content, valid file name
  @Test
  public void constructorTest1() {
    assertEquals("file1", file1.getFileName());
    assertEquals("/file1", file1.getPathname());
    assertEquals(JShell.getFileSystem().getRoot(), file1.getParent());
  }

  // test case: constructing a File object no content, invalid file name
  @Test
  public void constructorTest2() {
    File file = new File("abc!", "/abc!", JShell.getFileSystem().getRoot());
    assertEquals(null, file.getFileName());
    assertEquals(null, file.getPathname());
    assertEquals(null, file.getParent());
  }

  // test case: constructing a File object with content, valid file name
  @Test
  public void constructorTest3() {
    assertEquals("file2", file2.getFileName());
    assertEquals("this is file2", file2.getContent());
    assertEquals("/dir1/file2", file2.getPathname());
    assertEquals(dir1, file2.getParent());
  }

  // test case: constructing a File object with content, invalid file name
  @Test
  public void constructorTest4() {
    File file = new File("abc!", "/this is invalid", "/abc!",
        JShell.getFileSystem().getRoot());
    assertEquals(null, file.getFileName());
    assertEquals(null, file.getContent());
    assertEquals(null, file.getPathname());
    assertEquals(null, file.getParent());
  }

  // test case: generating the pathname for a file added to the root directory
  @Test
  public void makePathnameTest1() {
    assertEquals("/newFile",
        File.makePathname(JShell.getFileSystem().getRoot(), "newFile"));
  }

  // test case: generating the pathname for a file added to a directory (one
  // layer deep)
  @Test
  public void makePathnameTest2() {
    assertEquals("/dir1/newFile", File.makePathname(dir1, "newFile"));
  }

  // test case: generating the pathname for a file added to a directory (two
  // layers deep)
  @Test
  public void makePathnameTest3() {
    assertEquals("/dir1/newFile", File.makePathname(dir1, "newFile"));
  }

  // test case: generating the pathname for a file added to a directory (three
  // layers deep)
  @Test
  public void makePathnameTest4() {
    assertEquals("/dir1/dir2/newFile", File.makePathname(dir2, "newFile"));
  }

  // test case: generating the pathname for a file added to a directory (four
  // layers deep)
  @Test
  public void makePathnameTest5() {
    assertEquals("/dir1/dir2/dir3/newFile", File.makePathname(dir3, "newFile"));
  }

  // NOTE: Dependency injection cannot be applied when testing method
  // getFile() due to its use of static methods. Therefore, the
  // following test cases assume that the implementations of those static
  // methods are working.

  @Test
  public void getFileTest1() {
    File target = File.getFile("/".split("/"), "/");
    assertTrue(fileSystem.getRoot().equals(target));
  }

  @Test
  public void getFileTest2() {
    File target = File.getFile("/dir1".split("/"), "/dir1");
    assertTrue(target.equals(dir1));
  }

  @Test
  public void getFileTest3() {
    File target = File.getFile("file1".split("/"), "file1");
    assertTrue(target.equals(file1));
  }

  @Test
  public void getFileTest4() {
    File target = File.getFile("/dir1/dir2".split("/"), "/dir1/dir2");
    assertTrue(target.equals(dir2));
  }

  @Test
  public void getFileTest5() {
    JShell.getFileSystem().setCurrentDir(dir1);
    File target = File.getFile("file2".split("/"), "file2");
    assertTrue(target.equals(file2));
  }

  @Test
  public void getFileTest6() {
    JShell.getFileSystem().setCurrentDir(dir2);
    File target = File.getFile("dir3".split("/"), "dir3");
    assertTrue(target.equals(dir3));
  }

  @Test
  public void getFileTest7() {
    JShell.getFileSystem().setCurrentDir(dir1);
    File target = File.getFile("dir2/file3".split("/"), "dir2/file3");
    assertTrue(target.equals(file3));
  }

  @Test
  public void getFileTest8() {
    File target = File.getFile("/hey".split("/"), "/hey");
    assertEquals(null, target);
  }

  @Test
  public void getFileTest9() {
    File target = File.getFile("dir1/hey".split("/"), "dir1/hey");
    assertEquals(null, target);
  }

  @Test
  public void getFileTest10() {
    File target = File.getFile("/dir1/dir2/hey".split("/"), "/dir1/dir2/hey");
    assertEquals(null, target);
  }

  @Test
  public void getFileTest11() {
    JShell.getFileSystem().setCurrentDir(dir2);
    File target = File.getFile("dir3/hey".split("/"), "dir3/hey");
    assertEquals(null, target);
  }

  @Test
  public void getFileTest12() {
    JShell.getFileSystem().setCurrentDir(dir2);
    File target = File.getFile("dir3/hey/hi".split("/"), "dir3/hey/hi");
    assertEquals(null, target);
  }

  @Test
  public void getFileTest13() {
    File target =
        File.getFile("/dir1/dir2/hey/hi".split("/"), "/dir1/dir2/hey/hi");
    assertEquals(null, target);
  }

  @Test
  public void getFileTest14() {
    File target =
        File.getFile("//dir1/dir2/hey/hi".split("/"), "//dir1/dir2/hey/hi");
    assertEquals(null, target);
  }
}
