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

public class Cd extends Command {

  /**
   * Constructs a cd command object that contains arguments.
   * 
   * @param arguments The arguments from user in an String array
   */
  public Cd(String[] arguments) {
    super(arguments);
  }

  private Boolean checkForErrors() {
    ErrorChecker errorChecker = new ErrorChecker();
    if (!errorChecker.checkNumArguments(this, 1)) {
      return true;
    }
    // paths with more than two / together is not valid
    if (arguments[0].contains("//")) {
      System.err.println("cd: path does not exist");
      return true;
    }
    return false;
  }

  /**
   * String representation of Cd object
   * 
   * @return String representing cd command
   */
  public String toString() {
    return "cd";
  }

  /**
   * Executes the cd command, will return if there is any errors in arguments
   * and print out an error message. Changes the current directory to the
   * specified directory from user.
   */
  public String execute() {
    if (this.checkForErrors()) {
      return null;
    }
    if (arguments[0].contains(".") || arguments[0].contains("..")) {
      arguments[0] = Interpreter.interpretPathname(arguments[0]);
    }
    String[] pathnameArray = arguments[0].split("/");
    if (pathnameArray.length == 0) {
      JShell.getFileSystem().setCurrentDir(JShell.getFileSystem().getRoot());
      return null;
    }
    File file = File.getFile(pathnameArray, arguments[0]);
    if (file == null) {
      return ("cd: path does not exist");
    } else if (!(file instanceof Directory)) {
      return ("cd: path is not a directory");
    }
    JShell.getFileSystem().setCurrentDir((Directory) file);
    return JShell.setStatus();
  }

  /*
   * public static void main(String[] args) { Directory cat = new
   * Directory("cat", "/cat"); JShell.root.addContent(cat); Directory whale =
   * new Directory("whale", "/cat/whale"); cat.addContent(whale); // (dir)root
   * -> (dir)cat -> (dir)whale
   * 
   * String[] pwdTesting = {}; Pwd pwdTest = new Pwd(pwdTesting);
   * System.out.println("Test 1 : changing directory from root"); String[]
   * testing1 = {"cat"}; Cd test1 = new Cd(testing1); test1.execute();
   * pwdTest.execute();
   * System.out.println("---------------------------------------");
   * 
   * System.out.println("Test 2 : changing to parent directory with ..");
   * String[] testing2 = {".."}; Cd test2 = new Cd(testing2); test2.execute();
   * pwdTest.execute();
   * System.out.println("---------------------------------------");
   * 
   * System.out.println("Test 3 : changing to directory with absolute");
   * String[] testing3 = {"/cat/whale"}; Cd test3 = new Cd(testing3);
   * test3.execute(); pwdTest.execute();
   * System.out.println("---------------------------------------");
   * 
   * System.out.println("Test 4 : changing to directory with absolute and ..");
   * String[] testing4 = {"/cat/whale/.."}; Cd test4 = new Cd(testing4);
   * test4.execute(); pwdTest.execute();
   * System.out.println("---------------------------------------");
   * 
   * System.out.println("Test 5 : changing into a directory that doesn't exist"
   * ); String[] testing5 = {"/path/does/not/exist"}; Cd test5 = new
   * Cd(testing5); test5.execute(); pwdTest.execute();
   * System.out.println("---------------------------------------");
   * 
   * System.out.println("Test 6 : changing into a path that specifies a file");
   * File testingFile = new File("testingFile", "/testingFile", JShell.root);
   * JShell.root.addContent(testingFile); String[] testing6 = {"/testingFile"};
   * Cd test6 = new Cd(testing6); test6.execute(); pwdTest.execute();
   * System.out.println("---------------------------------------"); }
   */
}
