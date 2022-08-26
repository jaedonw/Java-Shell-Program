// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import driver.JShell;

/**
 * Represents the save command.
 * 
 * @author Jaedon Wong
 *
 */
public class Save extends Command {

  /**
   * Constructs an object that contains arguments for the save command.
   * 
   * @param arguments The command-line arguments supplied by the user.
   */
  public Save(String[] arguments) {
    super(arguments);
  }

  /**
   * Saves the state of a JShell session in a file on the user's computer.
   * 
   * @return An error message or null if nothing is to be printed.
   */
  public String execute() {
    ErrorChecker errorChecker = new ErrorChecker();

    if (!errorChecker.checkNumArguments(this, 1)) {
      return "usage: save FILENAME";
    }

    String fileName = arguments[0];
    JShellSession session =
        new JShellSession(JShell.getFileSystem(), JShell.getCommandHistory());

    try {
      File saveFile = new File(fileName);
      saveFile.createNewFile();
      FileOutputStream writeFile = new FileOutputStream(saveFile);
      ObjectOutputStream output = new ObjectOutputStream(writeFile);
      output.writeObject(session);
      output.flush();
      output.close();
    } catch (IOException e) {
      return "save: '" + fileName + "' is an invalid path";
    }
    return JShell.setStatus();
  }

  /**
   * @return The name of the command.
   */
  public String toString() {
    return "save";
  }
}
