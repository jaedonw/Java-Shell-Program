// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.lang.reflect.Method;
import driver.JShell;

/**
 * This class contains methods for printing the documentation for each supported
 * command to the console.
 * 
 * @author Steven Pham
 *
 */
public class Man extends Command implements OutputRedirection {

  private ErrorChecker error;
  private String absPath;
  private String filename;
  private String[] pathnameArray;
  private boolean redirect = false;
  private boolean syntax = false;

  /**
   * Constructs a Man command object with a string array of user arguments.
   * Checks arguments for file redirection, absolute paths, and "." and ".." and
   * modifies them accordingly.
   * 
   * @param arguments The array of user arguments
   */
  public Man(String[] arguments) {
    super(arguments);
    this.error = new ErrorChecker();

    if (arguments.length == 3) {
      if (arguments[1].equals(">") || arguments[1].equals(">>")) {
        redirect = true;
      }
      if (arguments[2].contains(".") || arguments[2].contains("..")) {
        arguments[2] = Interpreter.interpretPathname(arguments[2]);
      }
      if (!arguments[2].startsWith("/")) {
        absPath = Directory.relativeToAbs(arguments[2]);
      } else {
        absPath = arguments[2];
      }
      pathnameArray = absPath.split("/");
      if (pathnameArray.length != 0) {
        filename = pathnameArray[pathnameArray.length - 1];
      }
      if (arguments[1].contains(">>>") || arguments[2].contains("//")) {
        syntax = true;
        System.out.println("error: incorrect syntax");
      }
      if (!error.checkValidFileName(filename)) {
        syntax = true;
      }
    }
  }

  /**
   * Executes the man command. Prints an error message if no arguments are
   * provided, or if more than 1 argument is provided. Otherwise, it returns the
   * specified command's documentation, identified using a switch statement.
   * 
   * @return String containing documentation, or null if file redirection is
   *         requested.
   */
  public String execute() {
    if (syntax) {
      return null;
    }
    if (!error.checkMissingArguments(this, 1))
      ;
    else {
      String command = arguments[0];
      try {
        Method m = this.getClass().getDeclaredMethod(command);
        return redirectOutput((String) m.invoke(this));
      } catch (Exception e) {
        error.printInvalidCommand(command);
      }
      return null;
    }
    return null;
  }

  /**
   * The following methods contain the respective documentation for each
   * command.
   */
  private String exit() {
    String s1 = "NAME: exit\n";
    String s2 = "DESCRIPTION: Quits/Terminates the program.\n";
    return s1 + s2;
  }

  private String speak() {
    String s1 = "NAME: speak [STRING]\n";
    String s2 = "DESCRIPTION: Converts STRING to audible form.\n";
    String s3 = "If STRING is not specified, user can continuously\n";
    String s4 = "enter text, and exit using keyword QUIT\n";
    return s1 + s2 + s3 + s4;
  }

  private String mkdir() {
    String s1 = "NAME: mkdir DIR ...\n";
    String s2 = "DESCRIPTION: Make directory (or directories) DIR, if it "
        + "does not\n";
    String s3 = "already exist. DIR can be a full path or relative to current "
        + "directory.\n";
    return s1 + s2 + s3;
  }

  private String cd() {
    String s1 = "NAME: cd DIR\n";
    String s2 = "DESCRIPTION: Changes current directory to DIR.\n";
    String s3 = "DIR can be a full path or relative to current directory.\n";
    return s1 + s2 + s3;
  }

  private String ls() {
    String s1 = "NAME: ls [-R] [PATH ...]\n";
    String s2 = "DESCRIPTION: If [PATH ...] is empty, print all contents\n";
    String s3 = "of current working directory.\n";
    String s4 = "If -R is present, recursively list all subdirectories.\n";
    String s5 = "For each specified PATH:\n";
    String s6 = "If PATH specifies a file, print PATH.\n";
    String s7 = "If PATH specifies a directory, print the contents of PATH\n";
    return s1 + s2 + s3 + s4 + s5 + s6 + s7;
  }

  private String pwd() {
    String s1 = "NAME: pwd\n";
    String s2 = "DESCRIPTION: Prints the current working directory.\n";
    return s1 + s2;
  }

  private String pushd() {
    String s1 = "NAME: pushd DIR\n";
    String s2 = "DESCRIPTION: Saves the current working directory\n";
    String s3 = "by pushing onto the directory stack (Last In, First Out),\n";
    String s4 = "then changes the current working directory DIR.\n";
    return s1 + s2 + s3 + s4;
  }

  private String popd() {
    String s1 = "NAME: popd\n";
    String s2 = "DESCRIPTION: Removes the top entry from the directory\n";
    String s3 = "stack, and changes the working directory to that entry.\n";
    return s1 + s2 + s3;
  }

  private String history() {
    String s1 = "NAME: history [NUMBER]\n";
    String s2 = "DESCRIPTION: Prints out the user's most recent commands.\n";
    String s3 = "If NUMBER not specified, print all of the user's commands.\n";
    String s4 = "If NUMBER specified, print out last NUMBER commands.\n";
    return s1 + s2 + s3 + s4;
  }

  private String cat() {
    String s1 = "NAME: cat FILE1 [FILE2 ...]\n";
    String s2 = "DESCRIPTION: Displays the contents of FILE1 and other\n";
    String s3 = "files (FILE2, ...) concatenated in the shell\n";
    return s1 + s2 + s3;
  }

  private String echo() {
    String s1 = "NAME: echo STRING [>[>] OUTFILE]\n";
    String s2 = "DESCRIPTION: Prints STRING to shell if OUTFILE is not\n";
    String s3 = "specified. '>' redirects output to OUTFILE and\n";
    String s4 = "overwrites content with STRING. '>>' redirects\n";
    String s5 = "output to OUTFILE and appends content\n";
    String s6 = "with STRING. Creates OUTFILE if it does not exist.\n";
    return s1 + s2 + s3 + s4 + s5 + s6;
  }

  private String man() {
    String s1 = "NAME: man CMD\n";
    String s2 = "DESCRIPTION: Prints documentation for CMD.\n";
    return s1 + s2;
  }

  private String rm() {
    String s1 = "NAME: rm DIR\n";
    String s2 =
        "DESCRIPTION: Removes the directory DIR from the file " + "system.\n";
    return s1 + s2;
  }

  private String mv() {
    String s1 = "NAME: mv OLDPATH NEWPATH\n";
    String s2 = "DESCRIPTION: Move item OLDPATH to NEWPATH. Both OLDPATH\n";
    String s3 =
        "and NEWPATH may be relative to the current directory or " + "may\n";
    String s4 = "be full paths.If NEWPATH is a directory, move the item into\n";
    String s5 = "the directory.\n";
    return s1 + s2 + s3 + s4 + s5;
  }

  private String cp() {
    String s1 = "NAME: cp OLDPATH NEWPATH\n";
    String s2 = "DESCRIPTION: Copy item OLDPATH to NEWPATH. Both OLDPATH \n";
    String s3 = "and NEWPATH may be relative to the current directory or \n";
    String s4 = "may be full paths. If OLDPATH is a directory, recursively\n";
    String s5 = "copy the contents.\n";
    return s1 + s2 + s3 + s4 + s5;
  }

  private String curl() {
    String s1 = "NAME: curl URL\n";
    String s2 = "DESCRIPTION: Retrieve the file at URL and add it to the\n";
    String s3 = "current working directory.\n";
    return s1 + s2 + s3;
  }

  private String save() {
    String s1 = "NAME: save FILENAME\n";
    String s2 = "DESCRIPTION: Save the state of the entire shell to\n";
    String s3 = "location FILENAME on the user's computer.\n";
    return s1 + s2 + s3;
  }

  private String load() {
    String s1 = "NAME: load FILENAME\n";
    String s2 = "DESCRIPTION: Loads the contents of FILENAME and\n";
    String s3 = "reinitializes everything previously saved to FILENAME.\n";
    String s4 = "This function can only be called at launch.\n";
    return s1 + s2 + s3 + s4;
  }

  private String find() {
    String s1 = "NAME: find PATH ... -type [f|d] -name EXPRESSION";
    String s2 = "DESCRIPTION: Searches the directory (or directories) PATH\n";
    String s3 = "for files or directories, based on the -type specified.";
    String s4 =
        "File names and directory names must exactly match" + "EXPRESSION.\n";
    return s1 + s2 + s3 + s4;
  }

  private String tree() {
    String s1 = "NAME: tree\n";
    String s2 = "DESCRIPTION: Displays the entire file system as a tree.";
    return s1 + s2;
  }

  /**
   * @return The name of the command ("man")
   */
  public String toString() {
    return "man";
  }

  /**
   * Determines if file redirection is needed. If yes, then creates file if file
   * does not exist. Otherwise, it appends/overwrites depending on the choice
   * selected by the user. If file direction is not required, the method simply
   * returns the file text(s).
   * 
   * @param output The string of output to be redirected
   * @return String containing file text(s), or null if file redirection is
   *         requested by the user
   */
  @Override
  public String redirectOutput(String output) {
    if (redirect == true) {
      File f = File.getFile(pathnameArray, absPath);
      if (f == null) {
        Directory parentDir = Directory.getParentDir(absPath);
        if (parentDir == null)
          return null;
        File add = new File(filename, output, absPath, parentDir);
        parentDir.addContent(add);
      } else {
        if (!(f instanceof Directory)) {
          JShell.setStatus();
          if (arguments[1].equals(">")) {
            f.setContent(output);
          } else {
            f.setContent(f.getContent() + output);
          }
        } else {
          System.out.println("Error: path is a directory");
        }
      }
      return null;
    } else if (error.checkNumArguments(this, 1)) {
      JShell.setStatus();
      return output;
    }
    return null;
  }

  /*
   * tests for this class public static void main(String args[]) {
   * System.out.println("Test 1: no arguments"); String arg1[] = {}; Man m1 =
   * new Man(arg1); m1.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 2: exit"); String arg2[] = {"exit"}; Man m2 = new
   * Man(arg2); m2.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 3: speak"); String arg3[] = {"speak"}; Man m3 =
   * new Man(arg3); m3.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 4: mkdir"); String arg4[] = {"mkdir"}; Man m4 =
   * new Man(arg4); m4.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 5: cd"); String arg5[] = {"cd"}; Man m5 = new
   * Man(arg5); m5.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 6: ls"); String arg6[] = {"ls"}; Man m6 = new
   * Man(arg6); m6.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 7: pwd"); String arg7[] = {"pwd"}; Man m7 = new
   * Man(arg7); m7.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 8: pushd"); String arg8[] = {"pushd"}; Man m8 =
   * new Man(arg8); m8.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 9: popd"); String arg9[] = {"popd"}; Man m9 = new
   * Man(arg9); m9.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 10: history"); String arg10[] = {"history"}; Man
   * m10 = new Man(arg10); m10.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 11: cat"); String arg11[] = {"cat"}; Man m11 = new
   * Man(arg11); m11.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 12: echo"); String arg12[] = {"echo"}; Man m12 =
   * new Man(arg12); m12.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 13: man"); String arg13[] = {"man"}; Man m13 = new
   * Man(arg13); m13.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 14: nonexistent argument"); String arg14[] =
   * {"notrealcommand"}; Man m14 = new Man(arg14); m14.execute();
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 15: multiple arguments"); String arg15[] =
   * {"echo", "cat"}; Man m15 = new Man(arg15); m15.execute(); }
   */
}
