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
import a2.Pushd;
import driver.JShell;

public class PushdTest {

  Pushd command;
  FileSystem fileSystem;
  Directory dir1;
  File file1;
  Directory dir2;
  Directory dir3;

  // Setting up the file system that the Cp class depends on. It
  // is assumed that these dependencies are working.
  @Before
  public void setUp() {
    fileSystem = FileSystem.getFileSystemInstance();
    dir1 = new Directory("dir1", "/dir1", fileSystem.getRoot());
    file1 = new File("file1", "/file1", fileSystem.getRoot());
    dir2 = new Directory("dir2", "/dir1/dir2", dir1);
    dir3 = new Directory("dir3", "/dir1/dir2/dir3", dir2);

    fileSystem.getRoot().addContent(dir1);
    fileSystem.getRoot().addContent(file1);
    dir1.addContent(dir2);
    dir2.addContent(dir3);

    JShell.setFileSystem(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);
  }

  @Test
  public void constructorTest1() {
    String[] arguments = {"file1"};
    command = new Pushd(arguments);
    assertTrue(command.getArguments()[0].equals(arguments[0]));
  }

  // NOTE: Dependency injection cannot be applied when testing the constructor
  // of the Cp class because the static method Interpreter.interpretPathname()
  // is used in some cases. Therefore it is assumed that this dependency is
  // working in the following constructor test cases.

  @Test
  public void constructorTest2() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String[] arguments = {".."};
    command = new Pushd(arguments);
    assertEquals("/", command.getArguments()[0]);
  }

  @Test
  public void constructorTest3() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String[] arguments = {"."};
    command = new Pushd(arguments);
    assertEquals("/dir1", command.getArguments()[0]);
  }

  @Test
  public void constructorTest4() {
    JShell.getFileSystem().setCurrentDir(dir3);
    String[] arguments = {"./../dir3/../.././../dir1/dir2"};
    command = new Pushd(arguments);
    assertEquals("/dir1/dir2", command.getArguments()[0]);
  }

  // NOTE: Dependency injection cannot be applied when testing the execute()
  // method in the Cp class because the static method JShell.getFileSystem()
  // is used. Therefore it is assumed that this dependency is working in the
  // following test cases for the execute() method.

  @Test
  public void executeTest1() {
    String[] arguments = {"dir1"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir1));
    assertTrue(JShell.getFileSystem().getDirStack().get(0)
        .equals(JShell.getFileSystem().getRoot()));
  }

  @Test
  public void executeTest2() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String[] arguments = {"dir2"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir2));
    assertTrue(JShell.getFileSystem().getDirStack().get(0).equals(dir1));
  }

  @Test
  public void executeTest3() {
    JShell.getFileSystem().setCurrentDir(dir2);
    String[] arguments = {"dir3"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir3));
    assertTrue(JShell.getFileSystem().getDirStack().get(0).equals(dir2));
  }

  @Test
  public void executeTest4() {
    String[] call1 = {"dir1"};
    command = new Pushd(call1);
    command.execute(); // save root, change to dir1
    String[] call2 = {"dir2"};
    command = new Pushd(call2);
    command.execute(); // save dir1, change to dir2
    String[] call3 = {"dir3"};
    command = new Pushd(call3);
    command.execute(); // save dir2, change to dir3
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir3));
    assertTrue(JShell.getFileSystem().getDirStack().get(0).equals(dir2));
    assertTrue(JShell.getFileSystem().getDirStack().get(1).equals(dir1));
    assertTrue(JShell.getFileSystem().getDirStack().get(2)
        .equals(JShell.getFileSystem().getRoot()));
  }

  @Test
  public void executeTest5() {
    String[] arguments = {};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir()
        .equals(JShell.getFileSystem().getRoot()));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }

  @Test
  public void executeTest6() {
    String[] arguments = {"two", "arguments"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir()
        .equals(JShell.getFileSystem().getRoot()));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }

  @Test
  public void executeTest7() {
    String[] arguments = {"nonExistent"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir()
        .equals(JShell.getFileSystem().getRoot()));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }

  @Test
  public void executeTest8() {
    String[] arguments = {"/dir1/nonExistent"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir()
        .equals(JShell.getFileSystem().getRoot()));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }

  @Test
  public void executeTest9() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String[] arguments = {"dir2/nonExistent"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir1));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }

  @Test
  public void executeTest10() {
    String[] arguments = {"/dir1/dir2/dir3/nonExistent"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir()
        .equals(JShell.getFileSystem().getRoot()));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }

  @Test
  public void executeTest11() {
    String[] arguments = {"/dir1/dir2/dir3"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir3));
    assertTrue(JShell.getFileSystem().getDirStack().get(0)
        .equals(JShell.getFileSystem().getRoot()));
  }

  @Test
  public void executeTest12() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String[] arguments = {"dir2/dir3"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir().equals(dir3));
    assertTrue(JShell.getFileSystem().getDirStack().get(0).equals(dir1));
  }

  @Test
  public void executeTest13() {
    String[] arguments = {"file1"};
    command = new Pushd(arguments);
    command.execute();
    assertTrue(JShell.getFileSystem().getCurrentDir()
        .equals(JShell.getFileSystem().getRoot()));
    assertTrue(JShell.getFileSystem().getDirStack().isEmpty());
  }
}
