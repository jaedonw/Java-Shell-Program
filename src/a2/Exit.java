// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

/**
 * Represents the exit command.
 * 
 * @author Jaedon Wong
 */
public class Exit extends Command {

  /**
   * Constructs an object that contains arguments for the exit command.
   * 
   * @param arguments The command-line arguments supplied by the user.
   */
  public Exit(String[] arguments) {
    super(arguments); // calling the second constructor in class Command
  }

  /**
   * Stops execution of the shell program.
   */
  public String execute() {
    ErrorChecker error = new ErrorChecker();
    if (error.checkExcessArguments(this, 0)) { // exit takes 0 arguments
      System.exit(0); // terminate the program
    }
    return null;
  }

  /**
   * @return The name of the command.
   */
  public String toString() {
    return "exit";
  }

  /*
   * // this main function demonstrates the testing done for this class public
   * static void main(String[] args) { // testing the constructor String[]
   * arguments = {"test"}; Exit exitCommand = new Exit(arguments); if
   * (!exitCommand.arguments[0].equals("test")) {
   * System.out.println("error: arguments were not initialized correctly."); }
   * 
   * // testing execute() // invalid call exitCommand.execute();
   * System.out.println("If the first call didn't work, this is printed");
   * 
   * // valid call String[] noArguments = {}; exitCommand = new
   * Exit(noArguments); exitCommand.execute();
   * System.out.println("If the second call works, this is not printed"); }
   */
}
