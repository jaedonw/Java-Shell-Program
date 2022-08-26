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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import a2.FileSystem;
import a2.History;
import a2.JShellSession;
import a2.Save;
import driver.JShell;

public class SaveTest {

  Save cmd;
  FileSystem fileSystem;
  File saveTestFile;

  // Setting up the file system that Save depends on. It is assumed in these
  // tests that these dependencies are working.
  @Before
  public void setUp() {
    fileSystem = FileSystem.getFileSystemInstance();

    // adding some contents to the root directory
    Directory dir1 = new Directory("dir1", "/dir1", fileSystem.getRoot());
    fileSystem.getRoot().addContent(dir1);

    // changing the current working directory and dir stack
    fileSystem.setCurrentDir(dir1);
    fileSystem.getDirStack().add(fileSystem.getRoot());

    // adding to the command history
    History cmdHistory = new History();
    String[] args = {"dir1"};
    cmdHistory.addHistory("cd", args);
    JShell.setCommandHistory(cmdHistory);

    // this will be the test file we're saving
    saveTestFile = new File("SaveTestFile");

    JShell.setFileSystem(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);

    if (saveTestFile.exists()) {
      saveTestFile.delete();
    }
  }

  @Test // no arguments given
  public void executeTest1() {
    String[] args = {};
    cmd = new Save(args);
    assertEquals("usage: save FILENAME", cmd.execute());
  }

  @Test // too many arguments given
  public void executeTest2() {
    String[] args = {"too", "many"};
    cmd = new Save(args);
    assertEquals("usage: save FILENAME", cmd.execute());
  }

  @Test // saving a file, check that it exists
  public void executeTest3() {
    String[] args = {"SaveTestFile"};
    cmd = new Save(args);
    assertEquals(null, cmd.execute());
    assertTrue(saveTestFile.exists());
  }

  @Test // saving a file, checking the current working directory was saved
  public void executeTest4() {
    String[] args = {"SaveTestFile"};
    cmd = new Save(args);
    assertEquals(null, cmd.execute());

    // reading the file back
    JShellSession session = null;
    try {
      FileInputStream readFrom = new FileInputStream(saveTestFile);
      ObjectInputStream input = new ObjectInputStream(readFrom);
      session = (JShellSession) input.readObject();
      input.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertEquals("dir1", session.getFileSystem().getCurrentDir().getFileName());
    assertEquals("/dir1",
        session.getFileSystem().getCurrentDir().getPathname());
  }

  @Test // saving a file, checking the directory stack was saved
  public void executeTest5() {
    String[] args = {"SaveTestFile"};
    cmd = new Save(args);
    assertEquals(null, cmd.execute());

    // reading the file back
    JShellSession session = null;
    try {
      FileInputStream readFrom = new FileInputStream(saveTestFile);
      ObjectInputStream input = new ObjectInputStream(readFrom);
      session = (JShellSession) input.readObject();
      input.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertEquals("/",
        session.getFileSystem().getDirStack().get(0).getPathname());
  }

  @Test // saving a file, checking the command history was saved
  public void executeTest6() {
    String[] args = {"SaveTestFile"};
    cmd = new Save(args);
    assertEquals(null, cmd.execute());

    // reading the file back
    JShellSession session = null;
    try {
      FileInputStream readFrom = new FileInputStream(saveTestFile);
      ObjectInputStream input = new ObjectInputStream(readFrom);
      session = (JShellSession) input.readObject();
      input.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertEquals("cd dir1", session.getCommandHistory().getHistory().get(0));
    assertEquals(1, session.getCommandHistory().getHistory().size());
  }

  @Test // saving a file, checking the file system contents were saved
  public void executeTest7() {
    String[] args = {"SaveTestFile"};
    cmd = new Save(args);
    assertEquals(null, cmd.execute());

    // reading the file back
    JShellSession session = null;
    try {
      FileInputStream readFrom = new FileInputStream(saveTestFile);
      ObjectInputStream input = new ObjectInputStream(readFrom);
      session = (JShellSession) input.readObject();
      input.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    assertEquals("dir1",
        session.getFileSystem().getRoot().getAtIndex(0).getFileName());
  }

  @Test // checking an invalid pathname
  public void executeTest8() {
    String[] args = {"notValid/atAll"};
    cmd = new Save(args);
    assertEquals("save: 'notValid/atAll' is an invalid path", cmd.execute());
  }
}
