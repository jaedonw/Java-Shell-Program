// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * Representation of a command
 * 
 * @author Winson Yuan
 */

public class Mkdir extends Command {

  /**
   * Constructs a mkdir command object that contains arguments.
   * 
   * @param arguments The input from user in a string array
   */
  public Mkdir(String[] arguments) {
    super(arguments);
    for (int i = 0; i < arguments.length; i++) {
      if (arguments[i].contains(".") || arguments[i].contains("..")) {
        arguments[i] = Interpreter.interpretPathname(arguments[i]);
      }

    }
  }

  /**
   * String representation of Mkdir object
   * 
   * @return String representing mkdir command
   */
  public String toString() {
    return "mkdir";
  }

  private Directory getParentDirectory(String[] pathnameArray, int index) {
    String parentPathname = getParentDirPath(pathnameArray, index);
    return (Directory) File.getFile(parentPathname.split("/"), parentPathname);
  }

  private String getParentDirPath(String[] pathnameArray, int index) {
    if (pathnameArray.length == 2 && pathnameArray[0].equals("")) {
      return "/";
    }
    int trim = pathnameArray[pathnameArray.length - 1].length() + 1;
    return arguments[index].substring(0, arguments[index].length() - trim);
  }

  private Boolean checkForErrors(String[] pathnameArray, int index) {
    ErrorChecker error = new ErrorChecker();
    if (arguments[index].contains("//")) {
      System.out
          .println("mkdir: '" + arguments[index] + "' error with pathname");
      return true;
    }
    String fileName = "/";
    if (arguments[index].substring(arguments[index].length() - 1).equals("/")) {
      System.out.println("mkdir: error with pathname");
      return true;
    }
    if (pathnameArray != null && pathnameArray.length > 0) {
      fileName = pathnameArray[pathnameArray.length - 1];
    }
    if (!error.checkValidFileName(fileName)) {
      return true;
    } else if (error.checkIfPathExists(arguments[index])) {
      return true;
    } else if (!error.checkIfPathCanBeMade(arguments[index])) {
      return true;
    }
    return false;
  }

  /**
   * Executes the mkdir command, will return if there is any error in arguments
   * and print out an error message. Creates a new directory object and adds
   * that directory into the content of its parent directory
   */
  public String execute() {
    if (arguments.length == 0) {
      return ("mkdir: requires atleast one argument");
    }
    for (int i = 0; i < this.arguments.length; i++) {
      String[] pathnameArray = arguments[i].split("/");
      if (!this.checkForErrors(pathnameArray, i)) {
        Directory currentDir = JShell.getFileSystem().getCurrentDir();
        if (pathnameArray.length == 1) {
          String newPathname = File.makePathname(currentDir, arguments[i]);
          Directory newDir =
              new Directory(arguments[i], newPathname, currentDir);
          currentDir.addContent(newDir);
        } else {
          Directory parentDirectory = getParentDirectory(pathnameArray, i);
          String dirName = pathnameArray[pathnameArray.length - 1];
          String newPathname = File.makePathname(parentDirectory, dirName);
          Directory newDir =
              new Directory(dirName, newPathname, parentDirectory);
          parentDirectory.addContent(newDir);
        }
      }
    }
    return JShell.setStatus();
  }

  /*
   * public static void main(String[] args) {
   * 
   * System.out.println("Test 1 : making a directory with relative path");
   * String[] testing1 = {"cat"}; Mkdir test1 = new Mkdir(testing1);
   * test1.execute(); String[] lsTesting1 = {"/"}; Ls lsTest1 = new
   * Ls(lsTesting1); lsTest1.execute();
   * System.out.println("----------------------------------");
   * 
   * System.out.println("Test 2 : making a directory relative to path");
   * String[] testing2 = {"cat/dog"}; Mkdir test2 = new Mkdir(testing2);
   * test2.execute(); String[] lsTesting2 = {"/cat"}; Ls lsTest2 = new
   * Ls(lsTesting2); lsTest2.execute();
   * System.out.println("----------------------------------");
   * 
   * System.out.println("Test 3 : making a directory with full path"); String[]
   * testing3 = {"/cat/dog/whale"}; Mkdir test3 = new Mkdir(testing3);
   * test3.execute(); String[] lsTesting3 = {"/cat/dog"}; Ls lsTest3 = new
   * Ls(lsTesting3); lsTest3.execute();
   * System.out.println("----------------------------------");
   * 
   * System.out.println("Test 4 : making a directory with non-existent path");
   * String[] testing4 = {"/path/does/not/exist"}; Mkdir test4 = new
   * Mkdir(testing4); test4.execute();
   * System.out.println("----------------------------------");
   * 
   * System.out.println("Test 5 : too many arguments"); String[] testing5 =
   * {"/cat/dog/fish", "/cat/dog/whale/horse"}; Mkdir test5 = new
   * Mkdir(testing5); test5.execute();
   * System.out.println("----------------------------------"); }
   */

}
