// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import a2.File;
import driver.JShell;
import a2.Directory;

/**
 * This class contains methods for concatenating files and printing their
 * contents to the console.
 * 
 * @author Steven Pham
 *
 */
public class Cat extends Command implements OutputRedirection {

  private ErrorChecker error;
  private String absPath;
  private String filename;
  private String[] pathnameArray;
  private boolean redirect = false;
  private boolean syntax = false;

  /**
   * Constructs a Cat command object with a string array of user arguments.
   * Modifies arguments if they contain "." or "..". Checks if user requested
   * file redirection.
   * 
   * @param arguments The array of user arguments
   */
  public Cat(String[] arguments) {
    super(arguments);
    this.error = new ErrorChecker();
    if (arguments.length > 2 && (arguments[arguments.length - 2].equals(">")
        || arguments[arguments.length - 2].equals(">>"))) {
      redirect = true;
    }
    for (int i = 0; i < arguments.length; i++) {
      if (arguments[i].contains(".") || arguments[i].contains("..")) {
        arguments[i] = Interpreter.interpretPathname(arguments[i]);
      }
      if (arguments[i].contains("//") || arguments[i].contains(">>>")) {
        syntax = true;
      }
    }
    if (syntax) {
      System.out.println("error: incorrect syntax");
    }
  }

  /**
   * Executes the cat command. Will return an error if no arguments are
   * provided. Otherwise, the function loops through the list of files to be
   * concatenated and returns the contents of each file, with 3 newlines added
   * between each file.
   * 
   * @return String containing file text(s), or null if file redirection is
   *         requested
   */
  public String execute() {
    if (syntax) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    if (!error.checkMissingArguments(this, 1)) {
    } else {
      int loopLength = arguments.length;
      if (redirect == true) {
        loopLength = arguments.length - 2;
      }
      for (int i = 0; i < loopLength; i++) {
        if (!arguments[i].startsWith("/")) {
          absPath = Directory.relativeToAbs(arguments[i]);
        } else {
          if (!arguments[i].contains("/")
              && !error.checkValidFileName(arguments[i])) {
            continue;
          }
          absPath = arguments[i];
        }
        pathnameArray = absPath.split("/");
        File f = File.getFile(pathnameArray, absPath);
        if (f instanceof Directory) {
          System.out.println("cat: path is a directory\n");
        } else if (f == null) {
          System.out.println("cat: file does not exist\n");
        } else {
          if (redirect == true) {
            sb.append(f.getContent());
          } else {
            sb.append(f.getContent() + "\n\n\n");
          }
        }
      }
      return redirectOutput(sb.toString());
    }
    return null;
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
  public String redirectOutput(String output) {
    if (redirect == true) {
      if (!arguments[arguments.length - 1].startsWith("/")) {
        absPath = Directory.relativeToAbs(arguments[arguments.length - 1]);
      } else {
        absPath = arguments[arguments.length - 1];
      }
      pathnameArray = absPath.split("/");
      if (pathnameArray.length != 0) {
        filename = pathnameArray[pathnameArray.length - 1];
      }
      if (!error.checkValidFileName(filename)) {
        return null;
      }
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
          if (arguments[arguments.length - 2].equals(">")) {
            f.setContent(output);
          } else {
            f.setContent(f.getContent() + output);
          }
        } else {
          System.out.println("error: path is a directory");
        }
      }
      return null;
    }
    JShell.setStatus();
    return output;
  }

  /**
   * @return The name of the command ("cat")
   */
  public String toString() {
    return "cat";
  }
  /*
   * tests for this class public static void main(String args[]) { String[] arg1
   * = {"hello", ">", "file1"}; Echo e1 = new Echo(arg1); e1.execute();
   * 
   * String[] arg2 = {"hihi", ">", "file2"}; Echo e2 = new Echo(arg2);
   * e2.execute();
   * 
   * String[] arg3 = {"dir1"}; Mkdir m = new Mkdir(arg3); m.execute();
   * 
   * String[] f1 = {}; String[] f2 = {"/file1"}; String[] f3 = {"/notafile"};
   * String[] f4 = {"/dir1"}; String[] f5 = {"/file1", "/file2"}; String[] f6 =
   * {"/file1", "/notafile", "/file2"}; String[] f7 = {"/file1", "/dir1",
   * "/file2"};
   * 
   * System.out.println("Test 1: no file"); Cat c1 = new Cat(f1); c1.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 2: one file"); Cat c2 = new Cat(f2); c2.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 3: one file (does not exist)"); Cat c3 = new
   * Cat(f3); c3.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 4: directory"); Cat c4 = new Cat(f4);
   * c4.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 5: Multiple files"); Cat c5 = new Cat(f5);
   * c5.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 6: Multiple files (some don't exist)"); Cat c6 =
   * new Cat(f6); c6.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 7: Multiple files (some are directories)"); Cat c7
   * = new Cat(f7); c7.execute(); }
   */


}
