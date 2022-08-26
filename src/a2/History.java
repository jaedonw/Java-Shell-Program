// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.ArrayList;
import driver.JShell;

/**
 * History command implementation.
 * 
 * @author Aum Patel
 */
public class History extends Command
    implements java.io.Serializable, OutputRedirection {

  /**
   * history is an ArrayList that contains all past user input.
   */
  private ArrayList<String> history;
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a history command object and it initializes an empty ArrayList
   * to hold user history
   */
  public History() {
    this.history = new ArrayList<String>();
  }

  /**
   * Constructs a history command object with an String array representing
   * arguments and initializes the history ArrayList with another History
   * ArrayList of another history object.
   * 
   * @param arguments The user arguments entered when calling history command
   * @param inShell another History object that has initialized ArrayList
   */
  public History(String[] arguments, History inShell) {
    super(arguments);
    this.history = inShell.history;
  }

  /**
   * Gets the ArrayList tracking the history
   * 
   * @return the history ArrayList
   */
  public ArrayList<String> getHistory() {
    return history;
  }

  /**
   * Adds the user input to the History objects ArrayList to keep track of all
   * history. Input does not need to be valid user input.
   * 
   * @param command String of the command name the user tried to call
   * @param arguments String array of arguments the user inputed with command
   *        call
   */
  public void addHistory(String command, String[] arguments) {
    // Creates the user input to one nice string
    for (int i = 0; i < arguments.length; i++) {
      command = command + " " + arguments[i];
    }
    // Adds the user input to the history object ArrayList
    this.history.add(command);
  }

  /**
   * Executes the history command. Argument can be an optional single
   * non-negative integer. Will print errors when history command called with
   * invalid input.
   */
  public String execute() {
    ErrorChecker error = new ErrorChecker();
    String out = "";
    if (!(error.checkExcessArguments(this, 3))) {
      // Error if too many arguments taken
      return "Error! Usage: history [numOf] [[>|>>] filePath]";
    } else if (this.arguments.length == 1 || this.arguments.length == 3) {
      int numOf = this.history.size(); // How much history the user wants to see
      try {
        // Converts the argument to int
        numOf = Integer.parseInt(this.arguments[0]);
      } catch (Exception e) {
        return "Error! Invalid argument type. Should be integer.";
      }
      // Minimum of size of history and user input
      numOf = Math.min(numOf, this.history.size());
      if (numOf < 0) {
        return "Error! Argument must be >= 0";
      }
      for (int i = this.history.size() - numOf; i < this.history.size(); i++) {
        out = out + (String.valueOf(i + 1) + ". " + this.history.get(i)) + "\n";
      }
    } else { // No arguments given then print all history one entry a line
      for (int i = 0; i < this.history.size(); i++) {
        out = out + (String.valueOf(i + 1) + ". " + this.history.get(i)) + "\n";
      }
    }
    return this.redirectOutput(out);
  }

  /**
   * The textual representation of History command object. Which is just the
   * name of the command: "history"
   * 
   * @return String representing the command name
   */
  public String toString() {
    return "history";
  }

  // private void handleOutput(String output) {
  // Directory root = JShell.getFileSystem().getRoot();
  // File f = null;
  // if (this.arguments.length == 0 || this.arguments.length == 1) {
  // System.out.println(output);
  // } else if (this.arguments.length == 3 && !this.arguments[1].equals(">")
  // && !this.arguments[1].equals(">>")) {
  // System.err.println("Error! Usage: history numOf (>> | >) filePath");
  // } else if (this.arguments.length == 2 && !this.arguments[0].equals(">")
  // && !this.arguments[0].equals(">>")) {
  // System.err.println("Error! Usage: history (>> | >) filePath");
  // } else if (this.arguments.length == 2 || this.arguments.length == 3) {
  // int index = this.arguments.length - 1;
  // String fullPath = Directory.relativeToAbs(this.arguments[index]);
  // if ((f = root.fileAtPath(fullPath.substring(1).split("/"))) == null) {
  // System.err
  // .println("Error! " + this.arguments[index] + " is invalid path");
  // return;
  // }
  // f.redirectOutput(output, this.arguments[index - 1]);
  // }
  // }

  @Override
  public String redirectOutput(String output) {
    if (this.arguments.length == 0 || this.arguments.length == 1) {
      JShell.setStatus();
      return output;
    } else if (this.arguments.length == 3 && !this.arguments[1].equals(">")
        && !this.arguments[1].equals(">>")) {
      return "Error! Usage: history numOf (>> | >) filePath";
    } else if (this.arguments.length == 2 && !this.arguments[0].equals(">")
        && !this.arguments[0].equals(">>")) {
      return "Error! Usage: history (>> | >) filePath";
    } else if (this.arguments.length == 2 || this.arguments.length == 3) {
      int index = this.arguments.length - 2;
      String[] args =
          {output, this.arguments[index], this.arguments[index + 1]};
      Echo redirect = new Echo(args);
      return redirect.execute();
    }
    JShell.setStatus();
    return output;
  }


  // public static void main(String[] args) {
  //
  // //Testing addHistory method
  // History testHistory = new History();
  // String command = "history";
  // String[] argArr = {"23"};
  // testHistory.addHistory(command, argArr);
  // command = "command";
  // String[] argArr2 = {};
  // testHistory.addHistory(command, argArr2);
  // command = "ls";
  // String[] argArr3 = {"/dir1/dir2/./dir3", "..", ".", "testDir/dir22"};
  // testHistory.addHistory(command, argArr3);
  //
  // //Execute with no command
  // History temp = new History(argArr2, testHistory);
  // temp.execute();
  //
  // //Execute with large number
  // System.out.println("===================================================");
  // temp = new History(argArr, testHistory);
  // temp.execute();
  //
  // //Execute with non-int
  // System.out.println("===================================================");
  // String[] argArr4 = {"Hello"};
  // temp = new History(argArr4, testHistory);
  // temp.execute();
  //
  // //Execute with multiple args
  // System.out.println("===================================================");
  // String[] argArr5 = {"1", "2"};
  // temp = new History(argArr5, testHistory);
  // temp.execute();
  // }

}
