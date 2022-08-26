// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

/**
 * Represents the the data of a JShell session
 * 
 * @author Jaedon Wong
 *
 */
public class JShellSession implements java.io.Serializable {

  private FileSystem fileSystem;
  private History commandHistory;
  private static final long serialVersionUID = 1L;

  /**
   * Constructs an object that contains the mock file system and command history
   * of a JShell session.
   * 
   * @param fileSystem the file system of a JShell session.
   * @param commandHistory the command history of a JShell session.
   */
  public JShellSession(FileSystem fileSystem, History commandHistory) {
    this.fileSystem = fileSystem;
    this.commandHistory = commandHistory;
  }

  public FileSystem getFileSystem() {
    return this.fileSystem;
  }

  public History getCommandHistory() {
    return this.commandHistory;
  }
}
