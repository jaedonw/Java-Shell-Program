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
import a2.Cp;
import a2.Directory;
import a2.File;
import a2.FileSystem;
import driver.JShell;

public class CpTest {

  Cp command;
  FileSystem fs;
  Directory dir1;
  Directory dir2;
  Directory testDir;
  File testFile;

  // Setting up the file system that this class depends on. It is assumed that
  // these dependencies are working.
  @Before
  public void setUp() {
    fs = FileSystem.getFileSystemInstance();
    dir1 = new Directory("dir1", "/dir1", fs.getRoot());
    dir2 = new Directory("dir2", "/dir1/dir2", dir1);
    testDir = new Directory("testDir", "/testDir", fs.getRoot());
    testFile = new File("testFile", "OG testFile", "/testFile", fs.getRoot());

    fs.getRoot().addContent(dir1);
    fs.getRoot().addContent(testDir);
    fs.getRoot().addContent(testFile);
    dir1.addContent(dir2);

    JShell.setFileSystem(fs);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fs.getClass().getDeclaredField("singleReference"));
    field.setAccessible(true);
    field.set(null, null);
  }

  // test case: creating a new Cp object
  @Test
  public void constructorTest() {
    String[] arguments = {"file1", "/dir1"};
    command = new Cp(arguments);
    assertTrue(command.getArguments()[0].equals("file1"));
    assertTrue(command.getArguments()[1].equals("/dir1"));
  }

  // NOTE: Dependency injection cannot be applied when testing the execute()
  // method due to its use of static methods. Therefore, the
  // following test cases assume that the Cp class' dependencies are working.

  @Test // when no arguments are supplied
  public void executeTest1() {
    String[] arguments = {};
    command = new Cp(arguments);
    assertEquals("usage: cp OLDPATH NEWPATH", command.execute());
  }

  @Test // when too few arguments are supplied
  public void executeTest2() {
    String[] arguments = {"justOne"};
    command = new Cp(arguments);
    assertEquals("usage: cp OLDPATH NEWPATH", command.execute());
  }

  @Test // when too many arguments are supplied
  public void executeTest3() {
    String[] arguments = {"too", "many", "args"};
    command = new Cp(arguments);
    assertEquals("usage: cp OLDPATH NEWPATH", command.execute());
  }

  @Test // when OLDPATH doesn't exist
  public void executeTest4() {
    String[] arguments = {"/dne", "/dir1"};
    command = new Cp(arguments);
    assertEquals("error: path '/dne' cannot be copied", command.execute());
  }

  @Test // when OLDPATH is the root
  public void executeTest5() {
    String[] arguments = {"/", "dir1/dir2"};
    command = new Cp(arguments);
    assertEquals("error: path '/' cannot be copied", command.execute());
  }

  @Test // trying to copy a directory into one of its sub-directories
  public void executeTest6() {
    String[] arguments = {"dir1", "/dir1/dir2"};
    command = new Cp(arguments);
    assertEquals("error: cannot copy directory into child folder",
        command.execute());
  }

  @Test // when NEWPATH cannot be made
  public void executeTest7() {
    String[] arguments = {"dir1", "/dir1/can't/beMade"};
    command = new Cp(arguments);
    assertEquals("usage: cp OLDPATH NEWPATH", command.execute());
  }

  @Test // copying a directory to an existing directory, no overwrite
  public void executeTest8() {
    String[] arguments = {"/testDir", "/dir1/dir2"};
    command = new Cp(arguments);
    assertEquals(null, command.execute());
    Directory copy = (Directory) dir2.getAtIndex(0);
    assertEquals("testDir", copy.getFileName()); // same name as OG
    assertEquals("/dir1/dir2/testDir", copy.getPathname()); // diff pathname
    assertEquals(dir2, copy.getParent()); // diff parent
    assertTrue(copy.getDirectoryContents().isEmpty()); // same contents
    assertTrue(testDir != copy); // not the same references
  }

  @Test // copying a file to an existing directory, no overwrite
  public void executeTest9() {
    JShell.getFileSystem().setCurrentDir(dir1);
    String[] arguments = {"/testFile", "/dir1/dir2"};
    command = new Cp(arguments);
    assertEquals(null, command.execute());

    File copy = dir2.getAtIndex(0);
    assertEquals("testFile", copy.getFileName()); // same name as OG
    assertEquals("/dir1/dir2/testFile", copy.getPathname()); // diff pathname
    assertEquals(dir2, copy.getParent()); // diff parent
    assertEquals("OG testFile", copy.getContent()); // same contents
    assertTrue(testDir != copy); // not the same references
  }

  @Test // copying a directory, NEWPATH doesn't exist
  public void executeTest10() {
    String[] arguments = {"/testDir", "/dir1/dir2/newDir"};
    command = new Cp(arguments);
    assertEquals(null, command.execute());

    Directory copy = (Directory) dir2.getAtIndex(0);
    assertEquals("newDir", copy.getFileName()); // new name
    assertEquals("/dir1/dir2/newDir", copy.getPathname()); // new pathname
    assertEquals(dir2, copy.getParent()); // new parent
    assertTrue(copy.getDirectoryContents().isEmpty()); // same contents
    assertTrue(testDir != copy); // not the same references
  }

  @Test // copying a file, NEWPATH doesn't exist
  public void executeTest11() {
    String[] arguments = {"/testFile", "/dir1/dir2/newFile"};
    command = new Cp(arguments);
    assertEquals(null, command.execute());

    File copy = dir2.getAtIndex(0);
    assertEquals("newFile", copy.getFileName()); // new name
    assertEquals("/dir1/dir2/newFile", copy.getPathname()); // new pathname
    assertEquals(dir2, copy.getParent()); // new parent
    assertEquals("OG testFile", copy.getContent()); // same contents
    assertTrue(testFile != copy); // not the same references
  }

  @Test // copying a directory to a file
  public void executeTest12() {
    String[] arguments = {"dir1", "testFile"};
    command = new Cp(arguments);
    assertEquals("error: cannot copy a directory to a file", command.execute());
  }

  @Test // copying a file to a file with the same name, overwriting
  public void executeTest13() {
    File overwrite =
        new File("testFile", "overwrite me", "/dir1/dir2/testFile", dir2);
    dir2.addContent(overwrite);
    String[] arguments = {"/testFile", "/dir1/dir2/testFile"};
    command = new Cp(arguments);
    assertEquals(null, command.execute());
    assertEquals("OG testFile", overwrite.getContent());
  }


  @Test // copying a directory to a directory with the same name, overwriting
  public void executeTest14() {
    // this directory will be overwritten
    Directory overwrite = new Directory("testDir", "/dir1/testDir", dir1);
    dir1.addContent(overwrite);

    // we will check that the overwritten directory contains this file, after
    // initially being empty
    File temp = new File("temp", "/testDir/temp", testDir);
    testDir.addContent(temp);

    String[] arguments = {"testDir", "/dir1"};
    command = new Cp(arguments);

    assertEquals(null, command.execute());
    Directory check =
        (Directory) File.getFile("/dir1/testDir".split("/"), "/dir1/testDir");
    assertEquals("testDir", check.getFileName());
    assertEquals("/dir1/testDir", check.getPathname());
    assertEquals(dir1, check.getParent());
    assertEquals("temp", check.getAtIndex(0).getFileName());
    assertEquals("/dir1/testDir/temp", check.getAtIndex(0).getPathname());
    assertEquals("temp", check.getAtIndex(0).getFileName());
    assertEquals("/dir1/testDir/temp", check.getAtIndex(0).getPathname());
  }


  @Test // copying a directory somewhere with a file with the same name
  public void execute15() {
    File temp = new File("testDir", "/dir1/dir2/testDir", dir2);
    dir2.addContent(temp);

    String[] arguments = {"/testDir", "/dir1/dir2"};
    command = new Cp(arguments);
    
    // should be overwritten
    assertEquals(null, command.execute());
    assertTrue(dir2.getAtIndex(0) instanceof Directory);
    assertEquals("testDir", dir2.getAtIndex(0).getFileName());
    assertEquals("/dir1/dir2/testDir", dir2.getAtIndex(0).getPathname());
  }

  @Test // copying a file somewhere with a directory with the same name
  public void execute16() {
    Directory temp = new Directory("testFile", "/dir1/dir2/testFile", dir2);
    dir2.addContent(temp);

    String[] arguments = {"/testFile", "/dir1/dir2"};
    command = new Cp(arguments);
    
    // should be overwritten
    assertEquals(null, command.execute());
    assertFalse(dir2.getAtIndex(0) instanceof Directory);
    assertEquals("testFile", dir2.getAtIndex(0).getFileName());
    assertEquals("OG testFile", dir2.getAtIndex(0).getContent());
    assertEquals("/dir1/dir2/testFile", dir2.getAtIndex(0).getPathname());
  }

  @Test // copying a file to a directory containing a file with the same name
  public void execute17() {
    File temp = new File("testFile", "overwrite me", "/dir1/testFile", dir1);
    dir1.addContent(temp);

    String[] arguments = {"/testFile", "/dir1"};
    command = new Cp(arguments);

    assertEquals(null, command.execute());
    File target = File.getFile("/dir1/testFile".split("/"), "/dir1/testFile");
    assertEquals("OG testFile", target.getContent());
  }

  @Test // copying a directory and all of its contents
  public void execute18() {
    // filling testDir with contents
    File newFile1 = new File("newFile1", "hello", "/testDir/newFile1", testDir);
    Directory newDir1 = new Directory("newDir1", "/testDir/newDir1", testDir);
    File newFile2 =
        new File("newFile2", "world", "/testDir/newDir1/newFile2", newDir1);
    File newFile3 =
        new File("newFile3", "hey", "/testDir/newDir1/newFile3", newDir1);
    Directory newDir2 =
        new Directory("newDir2", "/testDir/newDir1/newDir2", newDir1);
    testDir.addContent(newFile1);
    testDir.addContent(newDir1);
    newDir1.addContent(newFile2);
    newDir1.addContent(newFile3);
    newDir1.addContent(newDir2);

    // copying testDir to /dir1/dir2
    String[] args = {"/testDir", "/dir1/dir2"};
    command = new Cp(args);
    assertEquals(null, command.execute());

    Directory copy = (Directory) dir2.getAtIndex(0);
    assertEquals("/dir1/dir2/testDir:\nnewFile1\nnewDir1\n", copy.getContent());
    assertEquals("/dir1/dir2/testDir/newDir1:\nnewFile2\nnewFile3\nnewDir2\n",
        copy.getAtIndex(1).getContent());
  }
}
