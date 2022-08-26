// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.ArrayList;
import java.util.Arrays;
import driver.JShell;

/**
 * This represents an directory
 * 
 * @author Aum Patel
 *
 */
public class Directory extends File {

  private static final long serialVersionUID = 1L;

  /**
   * contents is a ArrayList of File Objects that contain items stored in
   * directory.
   */
  private ArrayList<File> contents;

  /**
   * This is a constructor to construct an directory object by specifying a
   * name, path, and a parent directory.
   * 
   * @param dirName Name of the directory
   * @param path The path of the new directory
   * @param parent The parent directory to the new constructed directory
   */
  public Directory(String dirName, String path, Directory parent) {
    super(dirName, path, parent);
    this.contents = new ArrayList<File>();
  }

  /**
   * This is a constructor to construct an directory object by specifying a name
   * and a path. The parent directory is set to null.
   * 
   * @param dirName Name of the directory
   * @param path The path of the new directory
   */
  public Directory(String dirName, String path) {
    super(dirName, path, null);
    this.contents = new ArrayList<File>();
  }

  /**
   * The toString method will give textual representation of directory object
   * which is just the directory's name.
   * 
   * @return a string to represent the directory which is its name
   */
  public String toString() {
    return this.fileName;
  }

  /**
   * Gets the number of items stored in the directory.
   * 
   * @return The number of items stored in the directory
   */
  public int sizeOfContents() {
    return this.contents.size();
  }

  /**
   * Returns the File object at index position index at directory.
   * 
   * @param index is the position of file object in directory content list.
   * 
   * @return Object of type File at specified index returns null if invalid
   *         given.
   */
  public File getAtIndex(int index) {
    if (index >= this.contents.size() || index < 0) {
      return null;
    } else {
      return this.contents.get(index);
    }
  }

  /**
   * Takes a relative path string and returns a string that represents the same
   * path in absolute path terms.
   * 
   * @param relative string that represents the relative path.
   * 
   * @return String that represents the full path.
   */
  public static String relativeToAbs(String relative) {
    String absolute;
    Directory currentDir = JShell.getFileSystem().getCurrentDir();
    if (currentDir.pathname.equals("/")) {
      absolute = currentDir.pathname + relative;
    } else {
      absolute = currentDir.pathname + "/" + relative;
    }

    return absolute;
  }

  /**
   * Takes in an absolute path to an Object in the file system and returns its
   * parent directory.
   * 
   * @param absolutePath the absolute path of the File object to get parent of.
   * 
   * @return the parent directory of specified File Object given by absolute
   *         path
   */
  public static Directory getParentDir(String absolutePath) {
    int lastIndex = absolutePath.lastIndexOf("/");
    String parentPath = "/";
    if (lastIndex != 0 && lastIndex != -1) {
      parentPath = absolutePath.substring(0, lastIndex);
    }
    File parent = File.getFile(parentPath.split("/"), parentPath);
    if (!(parent instanceof Directory)) {
      System.err.println("Error! Cannot create file, path is not a directory");
      return null;
    }
    return (Directory) parent;
  }

  /**
   * Gives a string representation of the contents of a directory by having the
   * directory path followed by a colon and listing each content of the
   * directory one per line.
   * 
   * @return String that list the current directory and lists all its contents
   *         one per line.
   */
  public String getContent() {
    String s;
    s = this.pathname + ":\n";
    if (this.fileName.equals("root")) {
      s = "/:\n";
    }
    for (int i = 0; i < this.contents.size(); i++) {
      s = s + this.contents.get(i).fileName + "\n";
    }
    return s;
  }


  /**
   * @return the contents of directroy in a ArrayList of File objects
   */
  public ArrayList<File> getDirectoryContents() {
    return this.contents;
  }

  /**
   * Adds a File object to be stored by the directory.
   * 
   * @param newContent The File object to be stored in directory.
   */
  public void addContent(File newContent) {
    this.contents.add(newContent);
  }

  /**
   * Removes a File object in the directory.
   * 
   * @param oldContent The File object to be removed from directory.
   * 
   * @return true if the specified File object was in the Directory and was
   *         removed.
   */
  public boolean removeContent(File oldContent) {
    return this.contents.remove(oldContent);
  }

  private File dealWithDoubleDot(String[] pathArray) {
    if (this.parent != null && pathArray.length > 1) {
      return this.parent
          .fileAtPath(Arrays.copyOfRange(pathArray, 1, pathArray.length));
    } else if (this.parent == null && pathArray.length > 1
        && this.fileName.equals("root")) {
      return this
          .fileAtPath(Arrays.copyOfRange(pathArray, 1, pathArray.length));
    } else if (this.fileName.equals("root") && this.parent == null) {
      return JShell.getFileSystem().getRoot();
    } else {
      return this.parent; // Return null if can't go up a level
    }
  }

  /**
   * Returns the File or Directory object that is located at given absolute
   * path.
   * 
   * @param pathArray Array of Strings representing the absolute path split up
   *        by the "/" character.
   * 
   * @return File object at the absolute path, returns null if path does not
   *         exist.
   */
  public File fileAtPath(String[] pathArray) {
    if (pathArray.length == 0)
      return null; // Return null if empty path
    else if (pathArray.length == 1 && !(pathArray[0].equals("."))
        && !(pathArray[0].equals(".."))) {
      for (int i = 0; i < this.contents.size(); i++) {
        if (this.contents.get(i).fileName.equals(pathArray[0])) {
          return this.contents.get(i);
        }
      }
      return null; // Return null if file does not exit in current directory
    } else if (pathArray[0].equals(".") && pathArray.length > 1) {
      return this
          .fileAtPath(Arrays.copyOfRange(pathArray, 1, pathArray.length));
    } else if (pathArray[0].equals("."))
      return this;
    else if (pathArray[0].equals("..")) { // If path has ".." go up one level
      return this.dealWithDoubleDot(pathArray);
    } else { // Path does not start with "." or ".."
      for (int i = 0; i < this.contents.size(); i++) {
        if (this.contents.get(i).fileName.equals(pathArray[0])
            && this.contents.get(i) instanceof Directory) {
          return ((Directory) this.contents.get(i))
              .fileAtPath(Arrays.copyOfRange(pathArray, 1, pathArray.length));
        }
      }
      return null; // Return null if not found
    }
  }


  // this main function demonstrates the testing done for this class
  // public static void main(String[] args) {
  //
  // // testing the constructor
  // Directory testDir = new Directory("testDir", "/testDir", JShell.root);
  // if (!testDir.fileName.equals("testDir")
  // || !testDir.pathname.equals("/testDir")
  // || testDir.parent != JShell.root) {
  // System.out.println("error: testDir was not initialized correctly");
  // return;
  // }
  //
  // // testing the constructor and invalid file names
  // Directory testDir2 = new Directory("testDir2!", "/testDir2!", JShell.root);
  // if (testDir2.fileName != null || testDir2.pathname != null
  // || testDir2.parent != null) {
  // System.out.println("error: testDir2 has an invalid file name, but was "
  // + "still initialized");
  // return;
  // }
  //
  // // testing addContent and getAtIndex
  // testDir.addContent(new Directory("dir2", "/testDir/dir2", testDir));
  // String expected = "/testDir/dir2";
  // String actual = testDir.getAtIndex(0).pathname;
  // if (!expected.equals(actual)) {
  // System.out.println("error: w/ addContent or getAtIndex");
  // return;
  // }
  //
  // // testing getContent()
  // JShell.root.addContent(testDir);
  // Directory dir1 = new Directory("dir1", "/dir1", JShell.root);
  // JShell.root.addContent(dir1);
  // String expected2 = "/:\ntestDir\ndir1\n";
  // String actual2 = JShell.root.getContent();
  // if(!expected2.equals(actual2)) {
  // System.err.println("Error: getContent is incorrect");
  // return;
  // }
  //
  // //Testing fileAtPath
  // //Valid
  // String[] pathArr = "/testDir/dir2".substring(1).split("/");
  // File actualFile = JShell.root.fileAtPath(pathArr);
  // if(!("dir2".equals(((Directory)actualFile).toString()))) {
  // System.err.println("Error! fileAtPath incorrect");
  // return;
  // }
  // //Invalid
  // String[] pathArr2 = {"invalid", "path", "blah", "blah"};
  // actualFile = JShell.root.fileAtPath(pathArr2);
  // if(actualFile != null) {
  // System.err.println("Error! fileAtPath did not give back null");
  // return;
  // }
  //
  // //Testing relativeToAbs
  // String actual3 =
  // Directory.relativeToAbs("./testDir/./dir2/../.././dir1/.");
  // String expected3 = "/./testDir/./dir2/../.././dir1/.";
  // if(!(actual3.equals(expected3))) {
  // System.err.println("Error! relativeToAbs incorrect.");
  // return;
  // }
  //
  //
  // System.out.println("All tests were successful!");
  // }

}
