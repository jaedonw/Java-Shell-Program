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

/**
 * @author Aum Patel
 *
 */
public class DirectoryTest {

  Directory dir;
  MockFileSystem mfs;

  /**
   * Sets up mock file System
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
  }

  /**
   * Deallocates the dir instance member and destroys file system instance
   */
  @After
  public void tearDown() throws Exception {
    this.dir = null;
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

  /**
   * Test Constructor with 3 arguments
   */
  @Test
  public void testDirectoryConstructor1() {
    this.dir = new Directory("dir1", "/dir1", new Directory("root", "/"));
    assertEquals("dir1", dir.getFileName());
    assertEquals("/dir1", dir.getPathname());
    assertEquals("root", dir.getParent().getFileName());
    assertEquals("/", dir.getParent().getPathname());
  }

  /**
   * Test constructor with 2 arguments
   */
  @Test
  public void testDirectoryConstructor2() {
    this.dir = new Directory("dir2", "/dir2");
    assertEquals("dir2", dir.getFileName());
    assertEquals("/dir2", dir.getPathname());
    assertEquals(null, dir.getParent());
  }

  /**
   * Test addContent method when empty
   */
  @Test
  public void testAddContent1() {
    this.dir = new Directory("dir1", "/dir1");
    this.dir.addContent(new Directory("dir11", "/dir1/dir11", dir));
    assertEquals(1, dir.getDirectoryContents().size());
    assertEquals("/dir1/dir11",
        dir.getDirectoryContents().get(0).getPathname());
  }

  /**
   * Test addContent method with more than one adds
   */
  @Test
  public void testAddContent2() {
    this.dir = new Directory("dir1", "/dir1");
    this.dir.addContent(new Directory("dir33", "/dir1/dir33", dir));
    this.dir.addContent(new Directory("dir11", "/dir1/dir11", dir));
    assertEquals(2, dir.getDirectoryContents().size());
    assertEquals("/dir1/dir33",
        dir.getDirectoryContents().get(0).getPathname());
    assertEquals("/dir1/dir11",
        dir.getDirectoryContents().get(1).getPathname());
  }

  /**
   * Test sizeOfContents method
   */
  @Test
  public void testSizeOfContents() {
    this.dir = new Directory("dir1", "/dir1");
    this.dir.addContent(new Directory("dir33", "/dir1/dir33", dir));
    this.dir.addContent(new Directory("dir11", "/dir1/dir11", dir));
    assertEquals(2, dir.sizeOfContents());
  }

  /**
   * Test getAtIndex method with valid index
   */
  @Test
  public void testGetAtIndex1() {
    this.dir = new Directory("dir1", "/dir1");
    this.dir.addContent(new Directory("dir33", "/dir1/dir33", dir));
    this.dir.addContent(new Directory("dir11", "/dir1/dir11", dir));
    String name = "dir11";
    assertEquals(name, dir.getAtIndex(1).getFileName());
    assertEquals(2, dir.sizeOfContents());
  }

  /**
   * Test getAtIndex method with negative index
   */
  @Test
  public void testGetAtIndex2() {
    this.dir = new Directory("dir1", "/dir1");
    this.dir.addContent(new Directory("dir33", "/dir1/dir33", dir));
    this.dir.addContent(new Directory("dir11", "/dir1/dir11", dir));
    assertEquals(null, dir.getAtIndex(-1));
    assertEquals(2, dir.sizeOfContents());
  }

  /**
   * Test getAtIndex method with invalid index
   */
  @Test
  public void testGetAtIndex3() {
    this.dir = new Directory("dir1", "/dir1");
    this.dir.addContent(new Directory("dir33", "/dir1/dir33", dir));
    this.dir.addContent(new Directory("dir11", "/dir1/dir11", dir));
    assertEquals(null, dir.getAtIndex(2));
    assertEquals(2, dir.sizeOfContents());
  }

  /**
   * Test relativeToAbs method assuming at root
   */
  @Test
  public void testRelativeToAbs1() {
    this.mfs.mfs.setCurrentDir(this.mfs.mfs.getRoot());
    String actual = Directory.relativeToAbs("./../dir45/./dir46");
    assertEquals("/./../dir45/./dir46", actual);
  }

  /**
   * Test relativeToAbs method assuming not at root
   */
  @Test
  public void testRelativeToAbs2() {
    String actual = Directory.relativeToAbs("./../dir45/./dir46");
    assertEquals("/dir1/dir11/./../dir45/./dir46", actual);
  }

  /**
   * Test getPatentDir method valid input path
   */
  @Test
  public void testGetParentDir1() {
    Directory actual = Directory.getParentDir("/dir1/dir11");
    assertEquals("/dir1", actual.getPathname());
  }

  /**
   * Test getPatentDir method invalid input path (to file)
   */
  @Test
  public void testGetParentDir2() {
    Directory actual = Directory.getParentDir("/dir1/file2/dir33");
    assertEquals(null, actual);
  }

  /**
   * Test getContent method not at root
   */
  @Test
  public void testGetContent1() {
    this.dir = this.mfs.mfs.getCurrentDir();
    String actual = dir.getContent();
    assertEquals("/dir1/dir11:\n", actual);
  }

  /**
   * Test getContent method at root
   */
  @Test
  public void testGetContent2() {
    this.dir = this.mfs.mfs.getRoot();
    String actual = dir.getContent();
    assertEquals("/:\ndir1\ndir2\nfile1\n", actual);
  }

  /**
   * Test fileAtPath method with valid path (absolute path)
   */
  @Test
  public void testFileAtPath1() {
    this.dir = this.mfs.mfs.getRoot();
    String[] pathArr =
        "/dir1/./../dir2/../dir1/./file2".substring(1).split("/");
    String actual = this.dir.fileAtPath(pathArr).getFileName();
    assertEquals("file2", actual);
  }

  /**
   * Test fileAtPath method with path goes beyond root
   */
  @Test
  public void testFileAtPath2() {
    this.dir = this.mfs.mfs.getRoot();
    String[] pathArr = "/../../../../../.././.".substring(1).split("/");
    String actual = this.dir.fileAtPath(pathArr).getPathname();
    assertEquals("/", actual);
  }

  /**
   * Test fileAtPath method with invalid path
   */
  @Test
  public void testFileAtPath3() {
    this.dir = this.mfs.mfs.getRoot();
    String[] pathArr =
        "/dir1/dir11/../../dir2/../../dir33/.".substring(1).split("/");
    File actual = this.dir.fileAtPath(pathArr);
    assertEquals(null, actual);
  }

}
