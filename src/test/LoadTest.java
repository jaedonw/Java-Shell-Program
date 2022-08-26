package test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import a2.Directory;
import a2.FileSystem;
import a2.History;
import a2.JShellSession;
import a2.Load;
import driver.JShell;

public class LoadTest {

  Load l;
  FileSystem fileSystem;
  File saveTestFile;

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

    // saving file for testing
    JShellSession session =
        new JShellSession(JShell.getFileSystem(), JShell.getCommandHistory());
    try {
      File saveFile = new File("SaveTestFile");
      saveFile.createNewFile();
      FileOutputStream writeFile = new FileOutputStream(saveFile);
      ObjectOutputStream output = new ObjectOutputStream(writeFile);
      output.writeObject(session);
      output.flush();
      output.close();
    } catch (IOException e) {
    }
  }

  // deleting file after testing
  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);

    if (saveTestFile.exists()) {
      saveTestFile.delete();
    }
  }

  // constructor with no arguments
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    l = new Load(arguments);
    assertTrue(l.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    l = new Load(arguments);
    assertTrue(l.getArguments().equals(arguments));
  }

  // execute with no arguments
  @Test
  public void executeTest1() {
    String arguments[] = {};
    Load l1 = new Load(arguments);
    assertEquals(l1.execute(), null);
  }

  // execute with excess arguments
  @Test
  public void executeTest2() {
    String arguments[] = {"file1", "file2"};
    Load l1 = new Load(arguments);
    assertEquals(l1.execute(), null);
  }

  // execute with nonexistent file
  @Test
  public void executeTest3() {
    String arguments[] = {"randomfile"};
    Load l1 = new Load(arguments);
    assertEquals(l1.execute(), "file does not exist");
  }

  // simple load with existing file
  @Test
  public void executeTest4() {
    String arguments[] = {"saveTestFile"};
    Load l1 = new Load(arguments);
    assertEquals(l1.execute(), null);
  }

  // check history after loading
  @Test
  public void executeTest5() {
    String arguments[] = {"saveTestFile"};
    Load l1 = new Load(arguments);
    l1.execute();
    assertEquals("cd dir1", JShell.getCommandHistory().getHistory().get(0));
  }

  // check filesystem contents after loading
  @Test
  public void executeTest6() {
    String arguments[] = {"saveTestFile"};
    Load l1 = new Load(arguments);
    l1.execute();
    assertEquals("dir1",
        JShell.getFileSystem().getRoot().getAtIndex(0).getFileName());
  }

  // checking directory stack after loading
  @Test
  public void executeTest7() {
    String arguments[] = {"saveTestFile"};
    Load l1 = new Load(arguments);
    l1.execute();
    assertEquals("/",
        JShell.getFileSystem().getDirStack().get(0).getPathname());
  }

  // checking current working directory after loading
  @Test
  public void executeTest8() {
    String arguments[] = {"saveTestFile"};
    Load l1 = new Load(arguments);
    l1.execute();
    assertEquals("dir1", JShell.getFileSystem().getCurrentDir().getFileName());
    assertEquals("/dir1", JShell.getFileSystem().getCurrentDir().getPathname());
  }

  // checking ability to load after any command has been executed
  @Test
  public void executeTest9() {
    JShell.setStatus();
    String arguments[] = {"randomfile"};
    Load l1 = new Load(arguments);
    assertEquals(l1.execute(), "not able to load");
  }
}
