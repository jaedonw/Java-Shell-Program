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
import a2.FileSystem;

public class FileSystemTest {

  FileSystem fileSystem;

  @Before
  public void setUp() {
    fileSystem = FileSystem.getFileSystemInstance();
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);
  }

  // test case: constructing a new FileSystem object
  @Test
  public void constructorTest() {
    assertEquals("root", fileSystem.getRoot().getFileName());
    assertEquals("/", fileSystem.getRoot().getPathname());
    assertEquals(fileSystem.getRoot(), fileSystem.getCurrentDir());
    assertTrue(
        fileSystem.getDirStack() != null && fileSystem.getDirStack().isEmpty());
  }

  // test case: preserving the Singleton design pattern
  @Test
  public void singletonTest() {
    FileSystem origRef = fileSystem;
    fileSystem = FileSystem.getFileSystemInstance();
    fileSystem = FileSystem.getFileSystemInstance();
    fileSystem = FileSystem.getFileSystemInstance();
    fileSystem = FileSystem.getFileSystemInstance();
    assertTrue(origRef.equals(fileSystem));
  }
}
