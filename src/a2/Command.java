// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

/**
 * Representation of a command
 * 
 * @author Winson Yuan
 * @author Aum Patel
 */

public abstract class Command {

  /**
   * String array of arguments
   */
  protected String[] arguments;

  /**
   * Default constructor for objects
   */
  public Command() {}

  /**
   * Constructs and object of class Command with a string array of arguments.
   * 
   * @param arguments The string array from user input
   */
  public Command(String[] arguments) {
    this.arguments = arguments;
  }

  /**
   * Execute method for subclasses that inherit command
   */
  public abstract String execute();

  public String[] getArguments() {
    return this.arguments;
  }
}
