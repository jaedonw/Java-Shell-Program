// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.LinkedList;

import driver.JShell;

/**
 * Implementation of popd command.
 * 
 * @author Aum Patel
 *
 */

public class Popd extends Command {

  /**
   * Constructs Popd command object with String array of user arguments.
   * 
   * @param arguments The array of user arguments (if any) used when popd
   *        command called.
   */
  public Popd(String[] arguments) {
    super(arguments);
  }

  /**
   * The textual representation of popd command object. Which is just the name
   * of the command: "popd"
   * 
   * @return String representing the command name
   */
  public String toString() {
    return "popd";
  }

  /**
   * Executes the popd command. The command takes no arguments and will print if
   * any arguments given. Prints error if directory stack is empty. The command
   * changes the current working directory to the directory saved at the top of
   * the stack.
   */
  public String execute() {
    ErrorChecker error = new ErrorChecker();
    LinkedList<Directory> dirStack = JShell.getFileSystem().getDirStack();
    // Checks if no arguments given to popd command
    if (!(error.checkExcessArguments(this, 0))) {
      return "Error! Usage: popd";
    } else if (dirStack.size() == 0) { // Error if dirStack is empty
      return "Error! There is no saved directories in stack.";
    } else { // Changes directory with cd command to popped path
      Directory newDir = dirStack.pop();
      String[] arg = {newDir.pathname};
      Cd change = new Cd(arg);
      change.execute();
      JShell.setStatus();
      return null;
    }
  }

  // public static void main(String[] args) {
  // // testing execute
  // Directory dir1 = new Directory("dir1", "/dir1", JShell.root);
  // JShell.root.addContent(dir1);
  // Directory dir2 = new Directory("dir2", "/dir1/dir2", dir1);
  // dir1.addContent(dir2);
  //
  // //Testing popd with empty stack
  // String[] arg = {};
  // String[] arg22 = {"/dir1"};
  // Cd testCd = new Cd(arg22);
  // testCd.execute();
  // Popd testPop = new Popd(arg);
  // testPop.execute();
  // System.out.println("==============================================");
  //
  // String[] arguments = {"dir2"};
  // Pushd command = new Pushd(arguments);
  // command.execute();
  // Pwd test = new Pwd(arg);
  // test.execute();
  //
  // //Testing popd no argument
  // testPop.execute();
  // test.execute();
  // System.out.println("==============================================");
  //
  // //Testing popd with argument
  // testPop = new Popd(arguments);
  // testPop.execute();
  // }

}
