// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import driver.JShell;

/**
 * Represents the Cp command.
 * 
 * @author Jaedon Wong
 */
public class Cp extends Command {

  /**
   * Constructs an object that contains arguments for the cp command.
   * 
   * @param arguments The command-line arguments supplied by the user.
   */
  public Cp(String[] arguments) {
    super(arguments);
  }

  /**
   * Creates a copy of a file or directory somewhere in the file system.
   * 
   * @return An error message or null if nothing is to be printed to JShell.
   */
  public String execute() {
    ErrorChecker errorChecker = new ErrorChecker();

    // is there anything wrong with the # of arguments given?
    if (!errorChecker.checkNumArguments(this, 2)) {
      return "usage: cp OLDPATH NEWPATH";
    }

    // aliasing and interpreting the pathnames
    String oldPathname = Interpreter.interpretPathname(arguments[0]);
    String newPathname = Interpreter.interpretPathname(arguments[1]);

    // does OLDPATH exist OR is it the root?
    File oldPath = File.getFile(oldPathname.split("/"), oldPathname);
    if (oldPath == null || oldPath.pathname.equals("/")) {
      return "error: path '" + arguments[0] + "' cannot be copied";
    }

    // can NEWPATH be made? if not, stop execution
    if (!errorChecker.checkIfPathCanBeMade(newPathname)) {
      return "usage: cp OLDPATH NEWPATH";
    }

    // attempt retrieving NEWPATH
    File newPath = File.getFile(newPathname.split("/"), newPathname);

    if (newPath != null) { // if NEWPATH exists
      if (newPath instanceof Directory) { // and is a directory
        if (oldPath instanceof Directory) {
          return copyDirToDir((Directory) oldPath, (Directory) newPath);
        } else {
          return copyFileToDir(oldPath, (Directory) newPath);
        }
      } else { // and is a regular file
        if (oldPath instanceof Directory) {
          return "error: cannot copy a directory to a file";
        } else {
          newPath.setContent(oldPath.getContent());
        }
      }
    } else { // if NEWPATH doesn't exist
      return copy(oldPath, newPathname);
    }
    return JShell.setStatus();
  }

  private static String copyDirToDir(Directory oldPath, Directory targetDir) {
    // create copy of oldPath
    Directory copyDir = createDirectoryCopy((Directory) oldPath);
    copyDir.setParent(targetDir);
    updatePathnamesInDirectory(copyDir); // update pathnames

    for (int i = 0; i < targetDir.sizeOfContents(); i++) {
      File entry = targetDir.getAtIndex(i);
      if (entry.fileName.contentEquals(oldPath.fileName)) {
        // delete entry
        targetDir.removeContent(entry);
        // add oldPath to targetDir
        targetDir.addContent(copyDir);
        return JShell.setStatus();
      }
    }
    if (checkParentChild((Directory) oldPath, targetDir)) {
      return "error: cannot copy directory into child folder";
    }
    targetDir.addContent(copyDir);
    return JShell.setStatus();
  }

  private static String copyFileToDir(File oldPath, Directory targetDir) {
    String copyPathname = File.makePathname(targetDir, oldPath.fileName);
    File copyFile = new File(oldPath.fileName, oldPath.getContent(),
        copyPathname, targetDir);
    for (int i = 0; i < targetDir.sizeOfContents(); i++) {
      File entry = targetDir.getAtIndex(i);
      if (entry.fileName.contentEquals(oldPath.fileName)) {
        targetDir.removeContent(entry);
        targetDir.addContent(copyFile);
        return JShell.setStatus();
      }
    }
    targetDir.addContent(copyFile);
    return JShell.setStatus();
  }

  private static String copy(File oldPath, String newPathname) {
    // retrieve parent directory of copy
    String parentDirPathname = getParentDirPath(newPathname);
    String[] pathArr = parentDirPathname.split("/");
    Directory parentDir = (Directory) File.getFile(pathArr, parentDirPathname);

    // establish new file name, and pathname for copy
    String copyFileName =
        newPathname.substring(newPathname.lastIndexOf("/") + 1);
    String copyPathname = File.makePathname(parentDir, copyFileName);

    // create the copy with new pathname and new parent dir
    if (oldPath instanceof Directory) {
      // creates a deep copy, but pathnames have not been updated yet
      Directory copyDir = createDirectoryCopy((Directory) oldPath);
      copyDir.setFileName(copyFileName);
      copyDir.setParent(parentDir);
      if (checkParentChild((Directory) oldPath, copyDir)) {
        return "error: cannot copy directory into child folder";
      }
      updatePathnamesInDirectory(copyDir); // update pathnames
      parentDir.addContent(copyDir);
    } else {
      File newFile =
          new File(copyFileName, oldPath.getContent(), copyPathname, parentDir);
      parentDir.addContent(newFile);
    }
    return JShell.setStatus();
  }

  private static String getParentDirPath(String pathname) {
    String[] pathnameArray = pathname.split("/");
    if (pathnameArray.length == 2 && pathnameArray[0].equals("")) {
      return "/";
    }
    int trim = pathnameArray[pathnameArray.length - 1].length() + 1;
    return pathname.substring(0, pathname.length() - trim);
  }

  private static Directory createDirectoryCopy(Directory dir) {
    Directory dirCopy = null;
    try {
      // going to write dir to this stream
      ByteArrayOutputStream writeTo = new ByteArrayOutputStream();
      // writing dir
      ObjectOutputStream output = new ObjectOutputStream(writeTo);
      output.writeObject(dir);
      output.flush();
      output.close();

      // retrieve the data we just wrote
      byte[] data = writeTo.toByteArray();
      // going to read from this stream
      ByteArrayInputStream readFrom = new ByteArrayInputStream(data);
      // reading and creating the copy
      ObjectInputStream input = new ObjectInputStream(readFrom);
      dirCopy = (Directory) input.readObject();
    } catch (IOException | ClassNotFoundException e) {
      return null;
    }
    return dirCopy;
  }

  private static void updatePathnamesInDirectory(Directory dir) {
    // create a queue to store the objects that must be updated
    LinkedList<File> queue = new LinkedList<File>();
    queue.add(dir);

    while (!queue.isEmpty()) {
      File current = queue.remove();
      // update the pathname of current file object
      current.setPathname(File.makePathname(current.parent, current.fileName));
      if (current instanceof Directory) {
        queue.addAll(((Directory) current).getDirectoryContents());
      }
    }
  }

  private static boolean checkParentChild(Directory parent, Directory child) {
    Directory currentDir = child;
    while (true) {
      currentDir = currentDir.getParent();
      if (currentDir == parent) {
        return true;
      } else if (currentDir == JShell.getFileSystem().getRoot()) {
        return false;
      }
    }
  }

  /**
   * @return The name of the command.
   */
  public String toString() {
    return "cp";
  }
}
