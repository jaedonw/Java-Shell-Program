// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * Echo implementation.
 * 
 * @author Winson Yuan
 */
public class Echo extends Command implements OutputRedirection {

  private String fileName;
  private String absolutePath;
  private String[] pathnameArray;
  private boolean redirect = false;

  /**
   * Constructs a echo command object from string array of arguments.
   *
   * @param arguments The input from user in a string array
   */
  public Echo(String[] arguments) {
    super(arguments);
    if (arguments[2] != null) {
      redirect = true;
      if (arguments[2].contains(".") || arguments[2].contains("..")) {
        arguments[2] = Interpreter.interpretPathname(arguments[2]);
      }
      if (!arguments[2].startsWith("/")) {
        absolutePath = Directory.relativeToAbs(arguments[2]);
      } else {
        absolutePath = arguments[2];
      }
      pathnameArray = absolutePath.split("/");
      if (pathnameArray.length != 0) {
        fileName = pathnameArray[pathnameArray.length - 1];
      }
    }
  }

  /**
   * String representation of Echo object
   * 
   * @return String representing echo command
   */
  public String toString() {
    return "echo";
  }

  private boolean checkErrors() {
    ErrorChecker error = new ErrorChecker();
    if (!(error.checkExcessArguments(this, 3))) {
      return true;
    }
    if (arguments[2] != null) {
      if (arguments[2].contains("//")) {
        System.out.println("error: '" + arguments[2] + "' pathname is invalid");
        return true;
      }
    }

    if (arguments[0] == null) {
      System.err.println("usage: echo \"STRING\" [operator FILENAME]");
      return true;
    }
    if (arguments[1] != null) {
      if (arguments[2].equals("/")) {
        System.err.println("echo : Cannot modify the root directory");
        return true;
      }
      if (!error.checkValidFileName(this.fileName)) {
        return true;
      } else if (!error.checkIfPathCanBeMade(arguments[2])) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String redirectOutput(String output) {
    if (redirect) {
      File foundFile = File.getFile(pathnameArray, absolutePath);
      if (foundFile == null) {
        Directory parentDir = Directory.getParentDir(absolutePath);
        if (parentDir == null)
          return null;
        File add = new File(fileName, output, absolutePath, parentDir);
        parentDir.addContent(add);
      } else {
        if (!(foundFile instanceof Directory)) {
          if (arguments[1].equals(">")) {
            foundFile.setContent(output);
          } else {
            foundFile.setContent(foundFile.getContent() + output);
          }
        } else {
          return ("Error: path is a directory");
        }
      }
      return JShell.setStatus();
    }
    JShell.setStatus();
    return output;
  }

  /**
   * Executes the echo command, will return if error in arguments were found and
   * output an error message. If only one argument is passed into echo, it will
   * only output that. If three arguments are passed into echo, it will check if
   * file exist and if it does exist, it will append/overwrite based on user
   * input. If file not found, create a new file with that content.
   */
  public String execute() {
    if (this.checkErrors())
      return null;
    return redirectOutput(arguments[0]);
  }

  /*
   * public static void main(String[] args) { // interpreter class will make
   * sure there is string array is size 3
   * 
   * System.out.println(
   * "Test 1 : testing more than 3 arguments (expected error message)");
   * String[] testing1 = {"hello there", ">", "test", "extra"}; Echo test1 = new
   * Echo(testing1); test1.execute();
   * System.out.println("---------------------------");
   * 
   * System.out.println(
   * "Test 2 : testing no arguments passed (expected error message)"); String[]
   * testing2 = {null, null, null}; Echo test2 = new Echo(testing2);
   * test2.execute(); System.out.println("---------------------------");
   * 
   * System.out.println("Test 3 : testing by creating new file " +
   * "(expected new file \"bob\" with contents of \"hello there\")"); String[]
   * testing3 = {"hello there", ">", "bob"}; Echo test3 = new Echo(testing3);
   * test3.execute(); String[] cat3 = {"/bob"}; Cat catobject3 = new Cat(cat3);
   * catobject3.execute(); System.out.println("---------------------------");
   * 
   * System.out.println("Test 4 : testing by appending to an existing file" +
   * "(expected \"bob\" with contents of \"hello there, my name is \"");
   * String[] testing4 = {", my name is ", ">>", "bob"}; Echo test4 = new
   * Echo(testing4); test4.execute(); catobject3.execute();
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 5 : testing by overwriting an existing file" +
   * "(expected \"bob\" with contents of \"overwritten content\""); String[]
   * testing5 = {"overwritten content", ">", "bob"}; Echo test5 = new
   * Echo(testing5); test5.execute(); catobject3.execute();
   * System.out.println("---------------------------");
   * 
   * // checking with full paths and paths containing ".." or "." Directory cat
   * = new Directory("cat", "/cat"); JShell.root.addContent(cat); Directory
   * whale = new Directory("whale", "/cat/whale"); cat.addContent(whale); //
   * (dir)root -> (dir)cat / (file)bob -> (dir)whale
   * 
   * System.out.println("Test 6 : creating new file with full path"); String[]
   * testing6 = {"created with absolute", ">", "/cat/whale/john"}; Echo test6 =
   * new Echo(testing6); test6.execute(); String[] cat6 = {"/cat/whale/john"};
   * Cat catobject6 = new Cat(cat6); catobject6.execute();
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 7 : appending to existing file with full path");
   * String[] testing7 = {" now appending", ">>", "/cat/whale/john"}; Echo test7
   * = new Echo(testing7); test7.execute(); catobject6.execute();
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 8 : overwriting existing file with ."); String[]
   * testing8 = {"now overwriting bob file", ">", "/./bob"}; Echo test8 = new
   * Echo(testing8); test8.execute(); catobject3.execute();
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 9 : full path doesnt exist"); String[] testing9 =
   * {"failed", ">", "/path/doesnt/exist"}; Echo test9 = new Echo(testing9);
   * test9.execute(); System.out.println("---------------------------"); }
   */
}
