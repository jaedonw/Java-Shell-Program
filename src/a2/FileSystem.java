// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represents the file system.
 * 
 * @author Jaedon Wong
 */
public class FileSystem implements java.io.Serializable {

  private static FileSystem singleReference = null;
  private Directory root;
  private Directory currentDir;
  private LinkedList<Directory> dirStack;
  private static final long serialVersionUID = 1L;

  private FileSystem() {
    root = new Directory("root", "/");
    currentDir = root;
    dirStack = new LinkedList<Directory>();
  }

  /**
   * Constructs a new FileSystem object if there does not exist one already.
   * 
   * @return A reference to a FileSystem object.
   */
  public static FileSystem getFileSystemInstance() {
    if (singleReference == null) {
      singleReference = new FileSystem();
    }
    return singleReference;
  }

  public Directory getRoot() {
    return root;
  }

  public Directory getCurrentDir() {
    return currentDir;
  }

  public LinkedList<Directory> getDirStack() {
    return dirStack;
  }

  public void setCurrentDir(Directory directory) {
    currentDir = directory;
  }
}
