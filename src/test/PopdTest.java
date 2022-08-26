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
import a2.Popd;

/**
 * @author Aum Patel
 *
 */
public class PopdTest {

  MockFileSystem mfs;
  Popd comm;

  /**
   * Sets up the mock file system
   * 
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
  }

  /**
   * Destroys file system singleton instance
   * 
   * @throws java.lang.Exception
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
   * Test with one argument given
   */
  @Test
  public void testExecute1() {
    String[] arg = {"dir1"};
    comm = new Popd(arg);
    String exp = "Error! Usage: popd";
    assertEquals(exp, comm.execute());
  }

  /**
   * Test with multiple arguments given
   */
  @Test
  public void testExecute2() {
    String[] args = {"dir1", "dir2", "dir1/dir11"};
    comm = new Popd(args);
    String exp = "Error! Usage: popd";
    assertEquals(exp, comm.execute());
  }

  /**
   * Test with empty Directory Stack
   */
  @Test
  public void testExecut3() {
    this.mfs.mfs.getDirStack().pop();
    this.mfs.mfs.getDirStack().pop();
    String[] arg = {};
    comm = new Popd(arg);
    String exp = "Error! There is no saved directories in stack.";
    assertEquals(exp, comm.execute());
  }

  /**
   * Test with no arguments given
   */
  @Test
  public void testExecute4() {
    String[] arg = {};
    comm = new Popd(arg);
    String exp = null;
    String actual = comm.execute();
    assertEquals(exp, actual);
    assertEquals(1, this.mfs.mfs.getDirStack().size());
    assertEquals("/dir1", this.mfs.mfs.getCurrentDir().getPathname());
  }

  /**
   * Test with no arguments given but directory stack is 1
   */
  @Test
  public void testExecute5() {
    String[] arg = {};
    this.mfs.mfs.getDirStack().pop();
    comm = new Popd(arg);
    String exp = null;
    String actual = comm.execute();
    assertEquals(exp, actual);
    assertEquals(0, this.mfs.mfs.getDirStack().size());
    assertEquals("/dir1/dir11", this.mfs.mfs.getCurrentDir().getPathname());
  }

}
