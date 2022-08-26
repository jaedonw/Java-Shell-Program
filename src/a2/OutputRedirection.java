// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *******************************************************
package a2;

/**
 * This interface is responsible for output handling; output redirection when
 * requested to redirect the output of command to file, and if no redirection
 * requested the method gives back the error or output produced by the execution
 * of the command.
 * 
 * @author Aum Patel
 */
public interface OutputRedirection {

  /**
   * Responsible for the redirection of output of the executed command.
   * Determines if the output was requested to be redirected into a file, and if
   * so redirects it. If not redirected this method gives back the output or
   * error from the executed command.
   * 
   * @param output the string from execution of command (could be Error/Output)
   * @return String containing the STDOUT of the command being executed. String
   *         returned can also represent an error if one occurred. Returns null
   *         if the STDOUT was supposed to be redirected.
   */
  String redirectOutput(String output);

}
