package a2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import driver.JShell;

/**
 * This class contains methods for loading a previous JShell session.
 * 
 * @author Steven Pham
 *
 */
public class Load extends Command {

  /**
   * Constructs a pwd command object with a string array of user arguments.
   * 
   * @param arguments The array of user arguments
   */
  public Load(String[] arguments) {
    super(arguments);
  }

  /**
   * Executes the load command only if no successful commands have not been
   * entered. When load is called, it imports the filesystem and history that
   * was previously saved in a file.
   */
  public String execute() {
    ErrorChecker errorChecker = new ErrorChecker();
    if (!errorChecker.checkNumArguments(this, 1)) {
      return null; // stop execution if the # of arguments given is not 1
    }
    String fileName = arguments[0];
    JShellSession session = null;
    if (JShell.getStatus()) { // 1 or more successful commands have been
      // entered, do NOT load
      return "error: not able to load";
    } else {
      try {
        File saveFile = new File(fileName);
        if (saveFile.exists()) {
          FileInputStream readFrom = new FileInputStream(saveFile);
          ObjectInputStream input = new ObjectInputStream(readFrom);
          session = (JShellSession) input.readObject();
          input.close();
        } else { // file does not exist
          return "file does not exist";
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    }
    if (session != null) {
      JShell.setFileSystem(session.getFileSystem());
      JShell.setCommandHistory(session.getCommandHistory());
      JShell.setStatus();
    }
    return null;
  }

  /**
   * @return The name of the command ("load")
   */
  public String toString() {
    return "load";
  }
}
