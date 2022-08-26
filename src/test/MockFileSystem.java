// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import java.util.LinkedList;
import a2.Directory;
import a2.File;
import a2.FileSystem;
import driver.JShell;

/**
 * @author Aum Patel
 *
 */
public class MockFileSystem {

  protected FileSystem mfs;
  protected Directory dir1;
  protected Directory dir2;
  protected File file1;
  protected File file2;
  protected Directory dir11;


  /**
   * This constructs the mock file system for testing purposes. The structure
   * is:
   * -/ <- (root of mock file system) 
   *    -dir1 
   *        -file2 <-("Hi! This is file2.")
   *        -dir11 
   *    -dir2 
   *    -file1 <-("Hello World! This is file1.")
   */
  public MockFileSystem() {
    mfs = FileSystem.getFileSystemInstance();
    dir1 = new Directory("dir1", "/dir1", this.mfs.getRoot());
    dir2 = new Directory("dir2", "/dir2", this.mfs.getRoot());
    file1 = new File("file1", "/file1", this.mfs.getRoot());
    file2 = new File("file2", "/dir1/file2", dir1);
    dir11 = new Directory("dir11", "/dir1/dir11", dir1);
    // Directory dir1 = new Directory("dir1", "/dir1", this.mfs.getRoot());
    this.mfs.getRoot().addContent(dir1);
    /*
     * this.mfs.getRoot().addContent(new Directory("dir2", "/dir2",
     * this.mfs.getRoot()));
     */
    this.mfs.getRoot().addContent(dir2);
    // File file1 = new File("file1", "/file1", this.mfs.getRoot());
    file1.setContent("Hello World! This is file1.");
    // File file2 = new File("file2", "/dir1/file2", dir1);
    file2.setContent("Hi! This is file2.");
    this.mfs.getRoot().addContent(file1);
    dir1.addContent(file2);
    // Directory dir11 = new Directory("dir11", "/dir1/dir11", dir1);
    dir1.addContent(dir11);
    this.mfs.setCurrentDir(dir11);
    mfs.getDirStack().add(dir11);
    mfs.getDirStack().push(dir1);
    JShell.setFileSystem(mfs);
  }


}