// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * Represents a file.
 * 
 * @author Jaedon Wong
 */
public class File implements java.io.Serializable {

  /**
   * fileName: The name of the file. pathname: The absolute pathname
   * representing the location of the file in the file system. parent: The
   * directory that the file belongs to.
   */
  protected String fileName;
  protected String pathname;
  private String content;
  protected Directory parent;
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public File() {}

  /**
   * Constructs an object which contains the information of a file. If the file
   * name is invalid, nothing is initialized.
   * 
   * @param fileName The name of the file.
   * @param pathname The absolute pathname of the file.
   * @param parent The parent directory of the file.
   */
  public File(String fileName, String pathname, Directory parent) {
    ErrorChecker error = new ErrorChecker();

    // only initialize the file object if the file name is valid
    if (error.checkValidFileName(fileName)) {
      this.fileName = fileName;
      this.pathname = pathname;
      this.parent = parent;
    }
  }

  /**
   * Constructs an object which contains the information and contents of a file.
   * If the file name is invalid, nothing is initialized.
   * 
   * @param fileName The name of the file.
   * @param content The contents of the file.
   * @param pathname The absolute pathname of the file.
   * @param parent The parent directory of the file.
   */
  public File(String fileName, String content, String pathname,
      Directory parent) {
    ErrorChecker error = new ErrorChecker();

    // only initialize the file object if the file name is valid
    if (error.checkValidFileName(fileName)) {
      this.fileName = fileName;
      this.pathname = pathname;
      this.content = content;
      this.parent = parent;
    }
  }

  /**
   * @return The name of the file.
   */
  public String toString() {
    return this.fileName;
  }

  public String getContent() {
    return this.content;
  }

  public String getPathname() {
    return this.pathname;
  }

  public String getFileName() {
    return this.fileName;
  }

  public void setPathname(String pathname) {
    this.pathname = pathname;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setParent(Directory parent) {
    this.parent = parent;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Directory getParent() {
    return this.parent;
  }

  /**
   * Returns the file object that corresponds with a given pathname. This method
   * begins searching in either the root or the current working directory based
   * on the input pathname. If the pathname is not found, null is returned. This
   * method assumes the input pathname contains no . or .. characters.
   * 
   * @param pathnameArray The absolute or relative pathname of the file, split
   *        into an array.
   * @param pathname The absolute or relative pathname of the file.
   * @return The file object that contains the input pathname.
   */
  public static File getFile(String[] pathnameArray, String pathname) {
    if (pathname.contains("//")) {
      return null;
    }
    Directory root = JShell.getFileSystem().getRoot();
    Directory currentDir = JShell.getFileSystem().getCurrentDir();
    // append the pathname of the current directory to the input pathname
    String searchPathname = currentDir.pathname + "/" + pathname;

    if (Interpreter.isPathRelative(pathname)) {
      if (currentDir != root) {
        return ((File) currentDir).findFile(pathnameArray, searchPathname, -1);
      } else {
        return ((File) root).findFile(pathnameArray, "/" + pathname, -1);
      }
    } else {
      return ((File) root).findFile(pathnameArray, pathname, 0);
    }
  }

  private File findFile(String[] pathnameArray, String pathname, int depth) {
    // if the file being operated on matches the target pathname
    if (this.pathname.equals(pathname)) {
      return this;
    }
    if (this instanceof Directory) { // search the directory
      Directory dir = (Directory) this;
      for (int i = 0; i < dir.sizeOfContents(); i++) {
        File file = dir.getAtIndex(i);
        // if the pathnames match thus far, keep searching
        if (file.fileName.equals(pathnameArray[depth + 1])) {
          return file.findFile(pathnameArray, pathname, depth + 1);
        }
      }
    }
    return null;
  }

  /**
   * Returns the pathname of a file based on its parent directory.
   * 
   * @param parent The parent directory of the file.
   * @param fileName The name of the file.
   * @return The pathname of the file.
   */
  public static String makePathname(Directory parent, String fileName) {
    if (parent.pathname.equals("/")) {
      return "/" + fileName;
    } else {
      return parent.pathname + "/" + fileName;
    }
  }

  /*
   * // this main function demonstrates the testing done for this class public
   * static void main(String[] args) {
   * 
   * // testing the second constructor File testFile = new File("testFile",
   * "/testFile", JShell.root); if (!testFile.fileName.equals("testFile") ||
   * !testFile.pathname.equals("/testFile") || testFile.parent != JShell.root) {
   * System.out.println("error: testFile was not initialized correctly");
   * return; }
   * 
   * // testing the third constructor and invalid file names File testFile2 =
   * new File("testFile2!", "bad", "/testFile2!", JShell.root); if
   * (testFile2.fileName != null || testFile2.content != null ||
   * testFile2.pathname != null || testFile2.parent != null) {
   * System.out.println("error: testFile2 has an invalid file name, but was " +
   * "still initialized"); return; }
   * 
   * // testing the setters, getters and toString()
   * testFile.setContent("hello!\n"); String expected =
   * "File testFile has pathname /testFile and says hello!\n"; String actual =
   * "File " + testFile + " has pathname " + testFile.getPathname() +
   * " and says " + testFile.getContent(); if (!expected.equals(actual)) {
   * System.out.println("error: w/ setters, getters, toString()"); return; }
   * 
   * // testing getFile() JShell.root.addContent(testFile); Directory dir1 = new
   * Directory("dir1", "/dir1"); JShell.root.addContent(dir1);
   * dir1.addContent(new File("newFile", "/dir1/newFile", dir1));
   * dir1.addContent(new Directory("dir2", "/dir1/dir2"));
   * 
   * // 1. find a file, relative pathname // 2. find a directory, absolute
   * pathname // 3. find a file, absolute pathname, deeper in file system // 4.
   * find a directory, relative pathname, deeper in file system // 5. find a
   * file that doesn't exist if (getFile("testFile".split("/"), "testFile") ==
   * null || getFile("/dir1".split("/"), "/dir1") == null ||
   * getFile("/dir1/newFile".split("/"), "/dir1/newFile") == null ||
   * getFile("dir1/dir2".split("/"), "dir1/dir2") == null ||
   * getFile("/dir1/DNE".split("/"), "/dir1/DNE") != null) {
   * System.out.println("error: getFile couldn't find a file and/or found " +
   * "a non-existing file"); return; }
   * 
   * // testing makePathname() if (!makePathname(dir1,
   * "newPathname").equals("/dir1/newPathname")) {
   * System.out.println("error: makePathname() made the wrong pathname");
   * return; }
   * 
   * System.out.println("All tests were successful!"); }
   */
}
