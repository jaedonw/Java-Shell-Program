// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import driver.JShell;

/**
 * The Interpreter class contains various methods that are used to analyze
 * input, and return information that makes it easier to use that input.
 * 
 * @author Jaedon Wong
 */
public class Interpreter {

  private String userInput;
  private String commandName;
  private String[] arguments;
  private ErrorChecker errorChecker;

  /**
   * Constructs an object which contains the input supplied by the user on the
   * command line.
   * 
   * @param userInput The entire string entered by the user.
   * @param commandName The name of the command invoked by the user.
   * @param arguments The command-line arguments supplied by the user.
   */
  public Interpreter(String userInput, String commandName, String[] arguments) {
    this.userInput = userInput;
    this.commandName = commandName;
    this.arguments = arguments;
    this.errorChecker = new ErrorChecker();
  }

  /**
   * Determines which command was invoked by the user.
   * 
   * @return A Command object corresponding to the invoked command or null if
   *         the command does not exist.
   */
  public Command getCommand() {
    if (commandName.equals("history")) { // special case #1
      return null;
    } else if (commandName.equals("echo")) { // special case #2
      return this.interpretEcho();
    } else if (!this.userInput.isEmpty()) { // case for all others
      // get the name of the class
      String className = "a2." + this.commandName.toUpperCase().charAt(0)
          + this.commandName.substring(1);
      try {
        Constructor<?> constructor = Class.forName(className)
            .getDeclaredConstructor(new Class[] {String[].class});
        Command command =
            (Command) constructor.newInstance(new Object[] {this.arguments});
        if (command != null) {
          return command;
        }
        return errorChecker.printInvalidCommand(commandName);
      } catch (NoSuchMethodException | SecurityException
          | InstantiationException | IllegalAccessException
          | IllegalArgumentException | InvocationTargetException
          | ClassNotFoundException e) {
        return errorChecker.printInvalidCommand(this.commandName);
      }
    }
    return errorChecker.printInvalidCommand(this.commandName);
  }

  /**
   * Checks if a pathname is relative or not.
   * 
   * @param pathname The pathname.
   * @return true if the pathname is relative. false otherwise.
   */
  public static Boolean isPathRelative(String pathname) {
    String[] pathnameArray = pathname.split("/");
    // if pathname is absolute, the first '/' will become "" after splitting
    // if pathname is "/", it is the root pathname which is absolute
    if (pathnameArray.length == 0 || pathnameArray[0].equals("")) {
      return false;
    }
    return true;
  }

  private Echo interpretEcho() {
    String arg1 = null;
    String arg2 = null;
    String arg3 = null;
    this.userInput = this.userInput.trim().replaceAll(" +", " ");

    if (quotCount(userInput) > 2) {
      errorChecker.printEchoErrors();
      return null;
    } else if (userInput.length() >= 7) {
      // extracting the STRING component
      char space = userInput.charAt(4);
      char startQuot = userInput.charAt(5);
      int endQuotIndex = userInput.indexOf('"', 6);
      if (space == ' ' && startQuot == '"' && endQuotIndex != -1) {
        arg1 = userInput.substring(6, endQuotIndex);
      }
      // extracting the operator
      int spaceIndex = userInput.indexOf(' ', endQuotIndex);
      int nextSpaceIndex = userInput.indexOf(' ', spaceIndex + 1);
      if (arg1 != null && spaceIndex != -1 && nextSpaceIndex != -1) {
        arg2 = userInput.substring(spaceIndex + 1, nextSpaceIndex);
      }
      // extracting the file name
      if (arg2 != null && (arg2.equals(">") || arg2.equals(">>"))) {
        int start = nextSpaceIndex;
        nextSpaceIndex = userInput.indexOf(' ', nextSpaceIndex + 1);
        if (nextSpaceIndex == -1) {
          arg3 = userInput.substring(start + 1, userInput.length());
        }
      }
    }
    return this.assignArguments(arg1, arg2, arg3);
  }

  private Echo assignArguments(String arg1, String arg2, String arg3) {
    ErrorChecker error = new ErrorChecker();
    int length = userInput.length();

    // it only makes sense to run the echo command if either only the first
    // argument exists or all 3 exist
    if (arg1 != null) {
      if (length == 7 + arg1.length() || (arg2 != null && arg3 != null)) {
        String[] args = {arg1, arg2, arg3};
        return new Echo(args);
      }
    }
    error.printEchoErrors();
    return null;
  }

  /**
   * Interprets a pathname that contains . and/or .. characters.
   * 
   * @param pathname The interpreted absolute pathname.
   * @return An equivalent pathname without the . and/or .. characters.
   */
  public static String interpretPathname(String pathname) {
    String[] pathnameArray;
    String interpretedPathname = "";
    LinkedList<String> pathnameList = new LinkedList<String>();
    Directory currentDir = JShell.getFileSystem().getCurrentDir();

    // making sure pathnameArray refers to a full path
    if (isPathRelative(pathname)) {
      pathnameArray = File.makePathname(currentDir, pathname).split("/");
    } else {
      pathnameArray = pathname.split("/");
    }

    // do nothing if a . is encountered
    // return to the previous directory if a .. is encountered
    // keep adding to the new pathname otherwise
    for (int i = 0; i < pathnameArray.length; i++) {
      if (pathnameArray[i].equals(".")
          || (pathnameArray[i].equals("..") && pathnameList.size() < 1)) {
        continue;
      } else if (pathnameArray[i].equals("..") && pathnameList.size() >= 1) {
        pathnameList.removeLast();
      } else if (pathnameArray[i].equals("")) {
        pathnameList.add("/");
      } else {
        pathnameList.add(pathnameArray[i]);
      }
    }

    if (pathnameList.size() == 0) {
      return "/";
    }

    // rebuild the pathname
    while (!pathnameList.isEmpty()) {
      String current = pathnameList.poll();
      interpretedPathname += current;
      if (!pathnameList.isEmpty() && !current.equals("/")) {
        interpretedPathname += "/";
      }
    }
    return interpretedPathname;
  }

  private static int quotCount(String string) {
    String replaceWithQuotations = string.replace("\"", "");
    // subtract the length of the 'filtered' string from the length of the OG
    return string.length() - replaceWithQuotations.length();
  }

  public String getUserInput() {
    return this.userInput;
  }

  public String getCommandName() {
    return this.commandName;
  }

  public String[] getArguments() {
    return this.arguments;
  }

  public ErrorChecker getErrorChecker() {
    return this.errorChecker;
  }

  /*
   * // this main method demonstrates the testing done for this class public
   * static void main(String[] args) { // testing the constructor String[]
   * arguments = {"dir1"}; Interpreter interpreter = new Interpreter("hey dir1",
   * "hey", arguments);
   * 
   * if (!interpreter.userInput.equals("hey dir1") ||
   * !interpreter.commandName.equals("hey") || interpreter.arguments !=
   * arguments || interpreter.errorChecker == null) {
   * System.out.println("error: interpreter was not initialized correctly");
   * return; }
   * 
   * // testing getCommand() if (interpreter.getCommand() != null) {
   * System.out.println("error: interpreter got the wrong command"); return; }
   * 
   * // testing isPathRelative() { if (isPathRelative("/dir1")) {
   * System.out.println("error: interpreter thinks /dir1 is relative"); return;
   * } else if (isPathRelative("dir1")) {
   * System.out.println("not an error: dir1 is in fact relative"); }
   * 
   * // testing interpretPathname() String pathname =
   * "/.././dir1/.././dir1/dir2/./dir3/.."; if
   * (!interpretPathname(pathname).equals("/dir1/dir2")) {
   * System.out.println("error: interpretPathname() did not work"); return; }
   * 
   * System.out.println("All tests were successful"); }
   */
}
