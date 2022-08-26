// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import a2.Command;

public class MockCommand extends Command {

  public MockCommand(String[] arguments) {
    super(arguments);
  }

  @Override
  public String execute() {
    return "MockCommand has executed.";
  }

  public String toString() {
    return "MockCommand";
  }
}
