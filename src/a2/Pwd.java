// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * This class contains methods for printing the current working directory.
 * 
 * @author Steven Pham
 *
 */
public class Pwd extends Command implements OutputRedirection {

  private ErrorChecker error;
  private String absPath;
  private String filename;
  private String[] pathnameArray;
  private boolean redirect = false;
  private boolean syntax = false;

  /**
   * Constructs a pwd command object with a string array of user arguments.
   * Checks arguments for file redirection, absolute paths, and "." and ".." and
   * modifies them accordingly.
   * 
   * @param arguments The array of user arguments
   */
  public Pwd(String[] arguments) {
    super(arguments);
    error = new ErrorChecker();

    if (arguments.length == 2) {
      if (arguments[0].equals(">") || arguments[0].equals(">>")) {
        redirect = true;
      }
      if (arguments[1].contains(".") || arguments[1].contains("..")) {
        arguments[1] = Interpreter.interpretPathname(arguments[1]);
      }
      if (!arguments[1].startsWith("/")) {
        absPath = Directory.relativeToAbs(arguments[1]);
      } else {
        absPath = arguments[1];
      }
      pathnameArray = absPath.split("/");
      if (pathnameArray.length != 0) {
        filename = pathnameArray[pathnameArray.length - 1];
      }
      if (!error.checkValidFileName(filename)) {
        syntax = true;
      }
      if (arguments[0].contains(">>>") || arguments[1].contains("//")) {
        syntax = true;
        System.out.println("incorrect syntax");
      }
    }
  }

  /**
   * Executes the pwd command. Will return an error if the function is called
   * with arguments. If called with no arguments, it returns the current working
   * directory.
   * 
   * @return String containing current working directory, or null if file
   *         redirection is requested.
   */
  public String execute() {
    // prints current directory
    if (syntax) {
      return null;
    }
    String pathname = JShell.getFileSystem().getCurrentDir().pathname;
    return redirectOutput(pathname);
  }

  /**
   * @return The name of the command ("pwd")
   */
  public String toString() {
    return "pwd";
  }

  /**
   * Determines if file redirection is needed. If yes, then creates file if file
   * does not exist. Otherwise, it appends/overwrites depending on the choice
   * selected by the user. If file direction is not required, the method simply
   * returns the file text(s).
   * 
   * @param output The string of output to be redirected
   * @return String containing file text(s), or null if file redirection is
   *         requested by the user
   */
  @Override
  public String redirectOutput(String output) {
    if (redirect == true) {
      File f = File.getFile(pathnameArray, absPath);
      if (f == null) {
        Directory parentDir = Directory.getParentDir(absPath);
        if (parentDir == null)
          return null;
        File add = new File(filename, output, absPath, parentDir);
        parentDir.addContent(add);
      } else {
        if (!(f instanceof Directory)) {
          JShell.setStatus();
          if (arguments[0].equals(">")) {
            f.setContent(output);
          } else {
            f.setContent(f.getContent() + output);
          }
        } else {
          System.out.println("Error: path is a directory");
        }
      }
      return null;
    } else if (error.checkNumArguments(this, 0)) {
      JShell.setStatus();
      return output;
    }
    return null;
  }

  /*
   * tests for this class public static void main(String args[]) {
   * System.out.println("Test 1: proper function call"); String arg1[] = {}; Pwd
   * p1 = new Pwd(arg1); p1.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 2: function call with extra arguments"); String
   * arg2[] = {"thisisanargument"}; Pwd p2 = new Pwd(arg2); p2.execute(); }
   */
}


