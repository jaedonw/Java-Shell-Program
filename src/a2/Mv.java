// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.ArrayList;
import java.util.LinkedList;
import driver.JShell;

/**
 * Mv implementation.
 * 
 * @author Winson Yuan
 */

public class Mv extends Command {

  private String absolutePathOld;
  private String absolutePathNew;
  private String newFileName;
  private String oldFileName;
  private String[] oldPathnameArray;
  private String[] newPathnameArray;
  private File oldItem;
  private File newItem;
  private Directory parentOld;
  private Directory parentNew;
  private File remove;

  /**
   * Constructs a mv command object from string array of arguments.
   * 
   * @param arguments The input from user in a string array
   */
  public Mv(String[] arguments) {
    super(arguments);
    if (arguments.length == 2) {
      if (arguments[0].contains(".") || arguments[0].contains("..")) {
        arguments[0] = Interpreter.interpretPathname(arguments[0]);
      }
      if (!arguments[0].startsWith("/")) {
        absolutePathOld = Directory.relativeToAbs(arguments[0]);
      } else {
        absolutePathOld = arguments[0];
      }

      if (arguments[1].contains(".") || arguments[1].contains("..")) {
        arguments[1] = Interpreter.interpretPathname(arguments[1]);
      }
      if (!arguments[1].startsWith("/")) {
        absolutePathNew = Directory.relativeToAbs(arguments[1]);
      } else {
        absolutePathNew = arguments[1];
      }
      oldPathnameArray = absolutePathOld.split("/");
      newPathnameArray = absolutePathNew.split("/");
      if (newPathnameArray.length != 0) {
        newFileName = newPathnameArray[newPathnameArray.length - 1];
      }
      if (oldPathnameArray.length != 0) {
        oldFileName = oldPathnameArray[oldPathnameArray.length - 1];

      }
    }
  }

  /**
   * String representation of Mv object
   * 
   * @return String representing mv command
   */
  public String toString() {
    return "mv";
  }


  private Directory getParentDir(String path) {
    int lastIndex = path.lastIndexOf("/");
    String parentPath = "/";
    if (lastIndex != 0 && lastIndex != -1) {
      parentPath = path.substring(0, lastIndex);
    }
    File parent = File.getFile(parentPath.split("/"), parentPath);
    if (!(parent instanceof Directory)) {
      return null;
    }
    return (Directory) parent;
  }

  private void changePathNames(File moved) {
    LinkedList<File> queue = new LinkedList<File>();
    queue.add(moved);
    while (!queue.isEmpty()) {
      File current = queue.remove();

      if (current.parent.pathname.endsWith("/")) {
        current.pathname = current.parent.pathname + current.fileName;
      } else {
        current.pathname = current.parent.pathname + "/" + current.fileName;
      }
      if (current instanceof Directory) {
        queue.addAll(((Directory) current).getDirectoryContents());
      }
    }
  }

  private boolean checkErrors() {
    ErrorChecker error = new ErrorChecker();
    if (!(error.checkNumArguments(this, 2))) {
      return true;
    }
    if (oldItem == null) {
      System.err.println("mv: Old path file could not be found");
      return true;
    }
    boolean match = true;
    if (newPathnameArray.length == 0) {
      match = false;
    } else {
      for (int i = 0; i < oldPathnameArray.length; i++) {
        if (i == newPathnameArray.length) {
          match = false;
          break;
        }
        if (!newPathnameArray[i].equals(oldPathnameArray[i])) {
          match = false;
          break;
        }
      }
    }
    if (match) {
      System.err.println("mv: cannot move a parent directory");
      return true;
    }
    return false;
  }

  private void getFiles() {
    if (!absolutePathOld.contains("//")) {
      this.oldItem = File.getFile(absolutePathOld.split("/"), absolutePathOld);
      this.parentOld = getParentDir(absolutePathOld);
    }
    if (!absolutePathNew.contains("//")) {
      this.newItem = File.getFile(absolutePathNew.split("/"), absolutePathNew);
      this.parentNew = getParentDir(absolutePathNew);
    }
  }

  private boolean contains(Directory dir, String name, int index) {
    ArrayList<File> contents = dir.getDirectoryContents();
    for (int i = 0; i < contents.size(); i++) {
      File test = contents.get(i);
      if (test.fileName.equals(name)) {
        this.remove = test;
        return true;
      }
    }
    return false;
  }

  private void removeAdd(Directory oldP, Directory newP, File oldI) {
    oldP.removeContent(oldI);
    oldI.parent = newP;
    newP.addContent(oldI);
    changePathNames(oldI);
  }

  /**
   * Executes the mv command, will returns early if error found and could not
   * move file/directory. Changes the file/directory into a different directory,
   * rename if in same directory.
   */
  public String execute() {
    if (arguments.length == 2)
      getFiles();
    if (checkErrors())
      return null;
    if (newItem == null && parentNew != null && parentOld != null) {
      oldItem.fileName = newFileName;
      removeAdd(parentOld, parentNew, oldItem);
      return JShell.setStatus();
    }
    if (newItem == null || parentOld == null) {
      return ("mv: error new item not found");
    }
    if (!(newItem instanceof Directory)) {
      if (newItem instanceof File && !(oldItem instanceof Directory)) {
        oldItem.fileName = newFileName;
        parentNew.removeContent(newItem);
        removeAdd(parentOld, parentNew, oldItem);
        return JShell.setStatus();
      }
    } else {
      int index = 0;
      if (contains((Directory) newItem, oldFileName, index)) {
        ((Directory) newItem).removeContent(this.remove);
      }
      removeAdd(parentOld, ((Directory) newItem), oldItem);
      return JShell.setStatus();
    }
    return ("usage: mv OLDPATH NEWPATH");
  }
}
