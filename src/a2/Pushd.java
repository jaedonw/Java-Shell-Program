// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * Represents the pushd command.
 * 
 * @author Jaedon Wong
 */
public class Pushd extends Command {

  /**
   * Constructs an object that contains arguments for the pushd command.
   * 
   * @param arguments The command-line arguments supplied by the user.
   */
  public Pushd(String[] arguments) {
    super(arguments); // calling the second constructor in the Command class
    if (arguments.length == 1) {
      // pathname must be interpreted if it contains . and/or .. characters
      if (arguments[0].contains(".") || arguments[0].contains("..")) {
        arguments[0] = Interpreter.interpretPathname(arguments[0]);
      }
    }
  }

  /**
   * Saves the current directory in the directory stack, and then changes the
   * current directory to another directory.
   * 
   * @return null (this command does not produce output).
   */
  public String execute() {
    if (this.checkForErrors()) { // do not continue if there are errors
      return null;
    }

    Directory currentDir = JShell.getFileSystem().getCurrentDir();
    // add the current directory to the top of the stack
    JShell.getFileSystem().getDirStack().addFirst(currentDir);
    // change the current directory to the directory specified by the argument
    Cd changeDirectory = new Cd(arguments);
    changeDirectory.execute();
    return JShell.setStatus();
  }

  private Boolean checkForErrors() {
    ErrorChecker error = new ErrorChecker();
    // does this command have one and only one argument?
    if (!error.checkNumArguments(this, 1)) {
      return true;
    } else {
      // does the argument represent a valid existing directory?
      File dir = File.getFile(arguments[0].split("/"), arguments[0]);
      if (dir == null || !(dir instanceof Directory)) {
        System.out.println("'" + arguments[0] + "' is not a valid directory");
        return true;
      }
    }
    return false;
  }

  /**
   * @return The name of the command.
   */
  public String toString() {
    return "pushd";
  }

  /*
   * // this main function demonstrates the testing done for this class public
   * static void main(String[] args) { // testing the second constructor
   * String[] argumentsAbs = {"/dir1/dir2"}; String[] argumentsWithDots =
   * {"/../dir2"}; Pushd absolutePath = new Pushd(argumentsAbs); Pushd
   * pathWithDots = new Pushd(argumentsWithDots); if
   * (!absolutePath.arguments[0].equals("/dir1/dir2") ||
   * !pathWithDots.arguments[0].equals("/dir2")) {
   * System.out.println("error: arguments were not initialized correctly");
   * return; }
   * 
   * // testing execute Directory dir1 = new Directory("dir1", "/dir1");
   * JShell.root.addContent(dir1); Directory dir2 = new Directory("dir2",
   * "/dir2"); JShell.root.addContent(dir2);
   * 
   * String[] arguments = {"dir1"}; Pushd command = new Pushd(arguments);
   * 
   * // valid directory, empty stack command.execute(); if (JShell.currentDir !=
   * dir1 || JShell.dirStack.get(0) != JShell.root) {
   * System.out.println("error: pushd command failed"); return; }
   * 
   * // valid directory, non-empty stack arguments[0] = "/dir2";
   * command.execute(); if (JShell.currentDir != dir2 || JShell.dirStack.get(0)
   * != dir1) { System.out.println("error: pushd command failed"); return; }
   * 
   * // invalid directory arguments[0] = "/.././what?"; command.execute(); if
   * (JShell.currentDir != dir2 || JShell.dirStack.get(0) != dir1) {
   * System.out.println("error: pushd command failed"); return; }
   * 
   * System.out.println("All tests were successful"); }
   */
}
