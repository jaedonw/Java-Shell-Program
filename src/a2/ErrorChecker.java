// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

/**
 * The ErrorChecker class contains methods that are used to check for errors in
 * command invocations and arguments supplied to commands.
 * 
 * @author Jaedon Wong
 */
public class ErrorChecker {

  /**
   * Default constructor.
   */
  public ErrorChecker() {}

  /**
   * Prints an error message for an invalid command name and returns null.
   * 
   * @param commandName The name of the command entered by the user.
   * @return null
   */
  public Command printInvalidCommand(String commandName) {
    System.out.println(commandName + ": command not found");
    return null;
  }

  /**
   * Prints an error message for when the wrong number of arguments are supplied
   * to a command.
   * 
   * @param command The command object.
   * @param num The number of arguments.
   * @return true if the command has num arguments. false otherwise.
   */
  public Boolean checkNumArguments(Command command, int num) {
    if (command.arguments.length != num) {
      System.out.println(command + ": requires " + num + " argument(s)");
      return false;
    }
    return true;
  }

  /**
   * Prints an error message for when too few arguments are supplied to a
   * command.
   * 
   * @param command The command object.
   * @param num The number of arguments.
   * @return false if the command has less than num arguments. true otherwise.
   */
  public Boolean checkMissingArguments(Command command, int num) {
    if (command.arguments.length < num) {
      System.out.println(command + ": requires more arguments");
      return false;
    }
    return true;
  }

  /**
   * Prints an error message for when too many arguments are supplied to a
   * command.
   * 
   * @param command The command object.
   * @param num The number of arguments.
   * @return false if the command has more than num arguments. true otherwise.
   */
  public Boolean checkExcessArguments(Command command, int num) {
    if (command.arguments.length > num) {
      System.out.println(command + ": too many arguments given");
      return false;
    }
    return true;
  }

  /**
   * Checks if a file name contains any restricted characters.
   * 
   * @param fileName The name of the file.
   * @return true if the file name is valid. false otherwise.
   */
  public Boolean checkValidFileName(String fileName) {
    String[] invalidCharacters = {"/", ".", " ", "!", "@", "#", "$", "%", "^",
        "&", "*", "(", ")", "{", "}", "~", "|", "<", ">", "?"};
    for (int i = 0; i < invalidCharacters.length; i++) {
      if (fileName.contains(invalidCharacters[i])) {
        System.out.println(fileName + ": invalid file name");
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if a pathname already exists in the file system. This method assumes
   * that there are no . or .. characters in the pathname.
   * 
   * @param pathname The pathname.
   * @return true if the pathname exists in the file system. false otherwise.
   */
  public Boolean checkIfPathExists(String pathname) {
    String[] pathnameArray = pathname.split("/");

    if (File.getFile(pathnameArray, pathname) != null) {
      System.out.println("error: pathname '" + pathname + "' already exists");
      return true;
    }
    return false;
  }

  /**
   * Checks if a pathname can be created or not based on the existence of all
   * directories and/or files in the pathname. Whether the pathname exists or
   * not or already is irrelevant to this method. Also, whether or not the
   * pathname contains any invalid filenames is also irrelevant to this method.
   * 
   * @param pathname The pathname.
   * @return true if the pathname can be created. false otherwise.
   */
  public Boolean checkIfPathCanBeMade(String pathname) {
    String[] pathnameArray = pathname.split("/");
    String searchPathname = "";

    // build the pathname from the root, and check that it exists
    if (Interpreter.isPathRelative(pathname)) {
      for (int i = 0; i < pathnameArray.length - 1; i++) {
        if (i == 0) {
          searchPathname += pathnameArray[i];
        } else {
          searchPathname += "/" + pathnameArray[i];
        }
        if (File.getFile(searchPathname.split("/"), searchPathname) == null) {
          System.out
              .println("error: pathname '" + pathname + "' cannot be made");
          return false;
        }
      }
    } else {
      for (int i = 1; i < pathnameArray.length - 1; i++) {
        searchPathname += "/" + pathnameArray[i];
        if (File.getFile(searchPathname.split("/"), searchPathname) == null) {
          System.out
              .println("error: pathname '" + pathname + "' cannot be made");
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Prints the formatting rules for the echo command.
   */
  public void printEchoErrors() {
    System.out.println("usage: echo \"STRING\" [operator FILENAME]");
  }

  /*
   * // this main function demonstrates the testing done for this class public
   * static void main(String[] args) { // testing methods that check # of
   * arguments ErrorChecker error = new ErrorChecker(); String[] arguments =
   * {"1", "2"}; Command command = new Exit(arguments);
   * 
   * // does command have only one argument? No, so this shouldn't print. if
   * (error.checkNumArguments(command, 1)) {
   * System.out.println("error in checkNumArguments()"); return; }
   * 
   * // does command have over 3 arguments? No, so this shouldn't print. if
   * (!error.checkExcessArguments(command, 3)) {
   * System.out.println("error in checkExcessArguments()"); return; }
   * 
   * // does command have under 2 arguments? No, so this shouldn't print. if
   * (error.checkMissingArguments(command, 3)) {
   * System.out.println("error in checkMissingArguments()"); return; }
   * 
   * // testing checkValidFileName() if (error.checkValidFileName("!@#$%^&*("))
   * { System.out.println("error: an invalid file name got through"); return; }
   * 
   * // testing checkIfPathExists() Directory dir1 = new Directory("dir1",
   * "/dir1"); JShell.root.addContent(dir1);
   * 
   * if (!error.checkIfPathExists("/dir1")) {
   * System.out.println("error: checkIfPathExists() didn't find /dir1"); return;
   * }
   * 
   * // testing checkIfPathCanBeMade() // this path cannot be created because
   * /dir1/dir2 doesn't exist. So // checkIfPathCanBeMade() should not return
   * true if (error.checkIfPathCanBeMade("/dir1/dir2/dir3")) {
   * System.out.println("error: checkIfPathCanBeMade() returned true"); return;
   * }
   * 
   * System.out.println("All tests were successful"); }
   */
}
