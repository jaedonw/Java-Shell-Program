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
import a2.Ls;

/**
 * @author Aum Patel
 *
 */
public class LsTest {

  MockFileSystem mfs;
  Ls comm;

  /**
   * Sets up mock file System
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
    this.mfs.mfs.setCurrentDir(this.mfs.mfs.getRoot());
  }

  /**
   * Destroys the singleton instance of the mock file system
   * 
   * @throws Exception
   */
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

  /**
   * Execute with no arguments
   */
  @Test
  public void testExecute1() {
    String[] args = {};
    comm = new Ls(args);
    String exp = "/:\ndir1\ndir2\nfile1\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one valid argument (absolute path)
   */
  @Test
  public void testExecute2() {
    String[] args = {"/dir1"};
    comm = new Ls(args);
    String exp = "/dir1:\nfile2\ndir11\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one valid argument (relative path)
   */
  @Test
  public void testExecute3() {
    String[] args = {"dir1"};
    comm = new Ls(args);
    String exp = "/dir1:\nfile2\ndir11\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with multiple valid arguments (absolute/relative paths)
   */
  @Test
  public void testExecute4() {
    String[] args = {"/dir1", "dir2", "dir1/dir11"};
    comm = new Ls(args);
    String exp = "/dir1:\nfile2\ndir11\n\n/dir2:\n\n/dir1/dir11:\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one valid argument file (absolute path)
   */
  @Test
  public void testExecute5() {
    String[] args = {"/dir1/file2"};
    comm = new Ls(args);
    String exp = "file2\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one valid argument recursively (absolute path)
   */
  @Test
  public void testExecute6() {
    String[] args = {"-R", "/dir1"};
    comm = new Ls(args);
    String exp = "/dir1:\nfile2\ndir11\n\n/dir1/dir11:\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with multiple valid argument recursively (absolute/relative paths)
   */
  @Test
  public void testExecute7() {
    String[] args = {"-R", "/dir1", "dir2"};
    comm = new Ls(args);
    String exp = "/dir1:\nfile2\ndir11\n\n/dir1/dir11:\n\n/dir2:\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with path attempting to go beyond root of file system
   */
  @Test
  public void testExecute8() {
    String[] args = {"../../.."};
    comm = new Ls(args);
    String exp = "/:\ndir1\ndir2\nfile1\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with no arguments recursively
   */
  @Test
  public void testExecute9() {
    String[] args = {"-R"};
    comm = new Ls(args);
    String exp = "/:\ndir1\ndir2\nfile1\n\n/dir1:\nfile2\ndir11\n\n"
        + "/dir1/dir11:\n\n/dir2:\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one invalid argument recursively
   */
  @Test
  public void testExecute10() {
    String[] args = {"-R", "dir33"};
    comm = new Ls(args);
    String exp = "Error path /dir33 doesn't exist.\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one invalid argument
   */
  @Test
  public void testExecute11() {
    String[] args = {"dir33"};
    comm = new Ls(args);
    String exp = "Error path /dir33 doesn't exist.\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one invalid arguments rest are valid recursively
   */
  @Test
  public void testExecute12() {
    String[] args = {"-R", "/dir1", "dir33", "dir2/../../../dir2/."};
    comm = new Ls(args);
    String exp = "/dir1:\nfile2\ndir11\n\n/dir1/dir11:\n\n";
    exp += "Error path /dir33 doesn't exist.\n\n";
    exp += "/dir2:\n\n";
    assertEquals(exp, comm.execute());
  }

  /**
   * Execute with one invalid arguments rest are valid
   */
  @Test
  public void testExecute13() {
    String[] args = {"/dir1/dir11", "dir33", "dir2/../../../dir2/."};
    comm = new Ls(args);
    String exp = "/dir1/dir11:\n\n";
    exp += "Error path /dir33 doesn't exist.\n\n";
    exp += "/dir2:\n\n";
    assertEquals(exp, comm.execute());
  }

}
