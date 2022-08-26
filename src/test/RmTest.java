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
import a2.Rm;

/**
 * @author Aum Patel
 *
 */
public class RmTest {

  MockFileSystem mfs;
  Rm rmCom;

  /**
   * Set up the mock File System.
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
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
   * Test with no arguments
   */
  @Test
  public void testExecute1() {
    String[] args = {};
    rmCom = new Rm(args);
    String exp = "Error! Wrong number of arguments!\nUsage: rm DirPath";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with a lot of arguments
   */
  @Test
  public void testExecute2() {
    String[] args = {"/dir1", "/dir2"};
    rmCom = new Rm(args);
    String exp = "Error! Wrong number of arguments!\nUsage: rm DirPath";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with directory that does not exist
   */
  @Test
  public void testExecute3() {
    String[] args = {"dir33"};
    rmCom = new Rm(args);
    String exp = "Error! Invalid path: /dir1/dir11/dir33";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with trying to remove root directory
   */
  @Test
  public void testExecute4() {
    String[] args = {"/"};
    rmCom = new Rm(args);
    String exp = "Error! Cannot remove root of file system.";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with trying to remove non-directory
   */
  @Test
  public void testExecute5() {
    String[] args = {"/dir1/file2"};
    rmCom = new Rm(args);
    String exp = "Error! The rm command only removes a directory.";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with trying to remove current working directory
   */
  @Test
  public void testExecute6() {
    String[] args = {"."};
    rmCom = new Rm(args);
    String exp = "Error! Cannot remove current working directory.";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with trying to remove ancestor directory
   */
  @Test
  public void testExecute7() {
    String[] args = {".."};
    rmCom = new Rm(args);
    String exp = "Error! Cannot remove ancestor directory.";
    assertEquals(exp, rmCom.execute());
  }

  /**
   * Test with trying to remove valid directory (relative path)
   */
  @Test
  public void testExecute8() {
    String[] args = {"../../dir2"};
    rmCom = new Rm(args);
    assertEquals(null, rmCom.execute());
  }

  /**
   * Test with trying to valid directory (absolute path)
   */
  @Test
  public void testExecute9() {
    String[] args = {"/dir2"};
    rmCom = new Rm(args);
    assertEquals(null, rmCom.execute());
  }

}
