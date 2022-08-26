package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import a2.Command;
import a2.Directory;
import a2.Echo;
import a2.FileSystem;
import a2.Interpreter;
import a2.Mkdir;
import driver.JShell;

public class InterpreterTest {

  Interpreter interpreter;

  // Setting up the file system that method interpretPathname() depends on. It
  // is assumed that these dependencies are working.
  @Before
  public void setUp() {
    FileSystem fileSystem = FileSystem.getFileSystemInstance();
    Directory root = fileSystem.getRoot();
    Directory dir1 = new Directory("dir1", "/dir1", root);
    Directory dir2 = new Directory("dir2", "/dir1/dir2", dir1);
    Directory dir3 = new Directory("dir3", "/dir1/dir2/dir3", dir2);

    dir1.addContent(dir2);
    dir2.addContent(dir3);
    root.addContent(dir1);

    JShell.setFileSystem(fileSystem);
  }

  // test case: constructor initializes new Interpreter object correctly
  @Test
  public void constructorTest() {
    String[] arguments = {"arg1", "arg2"};
    interpreter = new Interpreter("cmd arg1 arg2", "cmd", arguments);
    assertEquals("cmd arg1 arg2", interpreter.getUserInput());
    assertEquals("cmd", interpreter.getCommandName());
    assertEquals("arg1", interpreter.getArguments()[0]);
    assertEquals("arg2", interpreter.getArguments()[1]);
    assertTrue(interpreter.getErrorChecker() != null);
  }

  // test case: command exists
  @Test
  public void getCommandTest1() {
    String[] arguments = {"dir1"};
    interpreter = new Interpreter("mkdir dir1", "mkdir", arguments);
    Command command = interpreter.getCommand();
    assertTrue(command instanceof Mkdir);
  }

  // test case: command doesn't exist
  @Test
  public void getCommandTest2() {
    String[] arguments = {"arg1"};
    interpreter = new Interpreter("invalid arg1", "invalid", arguments);
    Command command = interpreter.getCommand();
    assertEquals(null, command);
  }

  // test case: invalid command has a name similar to a valid command
  @Test
  public void getCommandTest3() {
    String[] arguments = {"arg1"};
    interpreter = new Interpreter("mkdirr arg1", "mkdirr", arguments);
    Command command = interpreter.getCommand();
    assertEquals(null, command);
  }

  // test case: history command (special case)
  @Test
  public void getCommandTest4() {
    String[] arguments = {"arg1"};
    interpreter = new Interpreter("history arg1", "history", arguments);
    Command command = interpreter.getCommand();
    assertEquals(null, command);
  }

  // test case: echo command (special case)
  @Test
  public void getCommandTest5() {
    String[] arguments = {"\"arg1\""};
    interpreter = new Interpreter("echo \"arg1\"", "echo", arguments);
    Command command = interpreter.getCommand();
    assertTrue(command instanceof Echo);
  }

  // test case: relative path
  @Test
  public void isPathRelativeTest() {
    assertTrue(Interpreter.isPathRelative("relative/pathname"));
  }

  // test case: absolute path
  @Test
  public void isPathRelativeTest2() {
    assertFalse(Interpreter.isPathRelative("/absolute/pathname"));
  }

  // test case: pathname of root directory
  @Test
  public void isPathRelativeTest3() {
    assertFalse(Interpreter.isPathRelative("/"));
  }

  // NOTE: Dependency injection cannot be applied when testing method
  // interpretPathname() due to its use of static methods. Therefore, the
  // following test cases assume that the implementations of those static
  // methods are working.

  @Test
  public void interpretPathnameTest1() {
    assertEquals("/", Interpreter.interpretPathname("/"));
  }

  public void interpretPathnameTest2() {
    assertEquals("/", Interpreter.interpretPathname(".."));
  }

  @Test
  public void interpretPathnameTest3() {
    assertEquals("/", Interpreter.interpretPathname("."));
  }

  @Test
  public void interpretPathnameTest4() {
    assertEquals("/", Interpreter.interpretPathname(".././../././.././"));
  }

  @Test
  public void interpretPathnameTest5() {
    assertEquals("/", Interpreter.interpretPathname("/dir1/.."));
  }

  @Test
  public void interpretPathnameTest6() {
    assertEquals("/dir1", Interpreter.interpretPathname("dir1/dir2/.."));
  }

  @Test
  public void interpretPathnameTest7() {
    assertEquals("/dir1/dir2",
        Interpreter.interpretPathname("/dir1/dir2/dir3/.."));
  }

  @Test
  public void interpretPathnameTest8() {
    assertEquals("/dir1/dir2",
        Interpreter.interpretPathname("dir1/./dir2/../dir2/././dir3/.."));
  }
}
