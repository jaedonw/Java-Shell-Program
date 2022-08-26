// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * Rm command implementation.
 * 
 * @author Aum Patel
 */
public class Rm extends Command {

  /**
   * Constructs a rm command object with string array of user arguments.
   * 
   * @param arguments The array of optional user arguments used when ls command
   *        called.
   */
  public Rm(String[] arguments) {
    super(arguments);
  }

  /**
   * The textual representation of Rm command object. Which is just the name of
   * the command: "rm"
   * 
   * @return String representing the command name
   */
  public String toString() {
    return "rm";
  }

  /**
   * Executes the rm command. Arguments can be an optional path (relative or
   * absolute). Will print errors when rm command called with invalid input. The
   * path entered will determine what directory to be removed, if no arguments
   * given the error will be printed.
   * 
   * @return
   */
  public String execute() {
    ErrorChecker error = new ErrorChecker();
    if (!(error.checkNumArguments(this, 1))) {
      return "Error! Wrong number of arguments!\nUsage: rm DirPath";
    }
    Directory rootDir = FileSystem.getFileSystemInstance().getRoot();
    if (!(this.arguments[0].startsWith("/"))) {
      this.arguments[0] = Directory.relativeToAbs(this.arguments[0]);
    }
    String[] pathArray = this.arguments[0].substring(1).split("/");
    File atPath = rootDir.fileAtPath(pathArray);
    String stat = Rm.checkIfRemoveable(atPath);
    if (this.arguments[0].equals("/")) {
      return "Error! Cannot remove root of file system.";
    } else if (atPath == null) {
      return "Error! Invalid path: " + this.arguments[0];
    } else if (atPath.pathname.equals("/")) {
      return "Error! Cannot remove root of file system.";
    } else if (stat == null && atPath instanceof Directory
        && atPath.parent != null) {
      if (!(atPath.parent.removeContent(atPath))) {
        return "Error! Something went wrong couldn't remove dir.";
      }
    }
    if (stat == null) {
      JShell.setStatus();
    }
    return stat;
  }

  private static String checkIfRemoveable(File dir) {
    // Gets current working directory
    Directory curr = FileSystem.getFileSystemInstance().getCurrentDir();
    if (dir == null) {
      return "Invalid path";
    } else if (!(dir instanceof Directory)) {
      return "Error! The rm command only removes a directory.";
    } else if (dir.pathname.equals("/")) {
      return "Error! Cannot remove root of file system.";
    } else if (dir.equals(curr)) {
      return "Error! Cannot remove current working directory.";
    } else {
      // Checks if the dir is not higher in terms of hierarchy compared to curr
      String[] pathRm = curr.pathname.substring(1).split("/");
      for (String dirToken : pathRm) {
        if (dirToken.equals(dir.fileName)) {
          return "Error! Cannot remove ancestor directory.";
        }
      }
      return null;
    }
  }
}
