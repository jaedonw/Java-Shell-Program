// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import driver.JShell;

/**
 * Ls command implementation.
 * 
 * @author Aum Patel
 */
public class Ls extends Command implements OutputRedirection {

  /**
   * Constructs a ls command object with string array of user arguments.
   * 
   * @param arguments The array of optional user arguments used when ls command
   *        called.
   */
  public Ls(String[] arguments) {
    super(arguments);
  }

  /**
   * The textual representation of Ls command object. Which is just the name of
   * the command: "ls"
   * 
   * @return String representing the command name
   */
  public String toString() {
    return "ls";
  }

  /**
   * Executes the ls command. Arguments can be an optional paths (relative or
   * absolute). Will print errors when ls command called with invalid input. The
   * paths entered will determine what contents to be printed, if no arguments
   * given the contents of current working directory will be printed.
   */
  public String execute() {
    Directory root = JShell.getFileSystem().getRoot();
    String out = "";
    if (this.arguments.length == 0 || (this.arguments.length == 2
        && (this.arguments[0].equals(">") || this.arguments[0].equals(">>")))) {
      out += JShell.getFileSystem().getCurrentDir().getContent() + "\n";
    } else if (this.arguments[0].equals("-R")) {
      out = this.executeRecur(out, 0);
    } else {
      for (int i = 0; i < this.arguments.length; i++) {
        if (!this.arguments[i].startsWith("/")
            && !this.arguments[i].contains(">")) {
          this.arguments[i] = Directory.relativeToAbs(this.arguments[i]);
        }
        File f = root;
        if (this.arguments[i].equals("/"))
          f = root;
        else {
          f = root.fileAtPath(this.arguments[i].substring(1).split("/"));
        }
        if (f == null && !this.arguments[i].contains(">")) {
          out += "Error path " + this.arguments[i] + " doesn't exist.\n\n";
        } else if (f instanceof Directory) {
          out += ((Directory) f).getContent() + "\n";
        } else if (this.arguments[i].contains(">")) {
          break;
        } else {
          out += f.toString() + "\n";
        }
      }
    }
    return this.redirectOutput(out);
  }

  private String executeRecur(String output, int depth) {
    Directory root = JShell.getFileSystem().getRoot();
    String out = output;
    if ((this.arguments.length == 1 && this.arguments[0].equals("-R"))
        || (this.arguments.length == 3 && this.arguments[1].contains(">"))) {
      Directory curr = JShell.getFileSystem().getCurrentDir();
      out += curr.getContent() + "\n";
      for (File x : curr.getDirectoryContents()) {
        if (x instanceof Directory) {
          String[] arg = {x.pathname};
          Ls recurr = new Ls(arg);
          out = recurr.executeRecur(out, depth + 1);
        }
      }
    } else if (this.arguments.length == 1 && !this.arguments[0].equals("-R")) {
      String[] fullPath = this.arguments[0].substring(1).split("/");
      Directory dir = (Directory) root.fileAtPath(fullPath);
      out += dir.getContent() + "\n";
      for (File x : dir.getDirectoryContents()) {
        if (x instanceof Directory) {
          String[] arg = {x.pathname};
          Ls recurr = new Ls(arg);
          out = recurr.executeRecur(out, depth + 1);
        }
      }
    } else {
      out = this.executeRecurWithArgs(out);
    }
    return out;
  }

  private String executeRecurWithArgs(String output) {
    Directory root = JShell.getFileSystem().getRoot();
    String out = output;
    for (int i = 1; i < this.arguments.length; i++) {
      if (!this.arguments[i].startsWith("/")
          && !this.arguments[i].contains(">")) {
        this.arguments[i] = Directory.relativeToAbs(this.arguments[i]);
      }
      File f = root;
      if (this.arguments[i].equals("/"))
        f = root;
      else {
        f = root.fileAtPath(this.arguments[i].substring(1).split("/"));
      }
      if (f == null && !this.arguments[i].contains(">")) {
        out += "Error path " + this.arguments[i] + " doesn't exist.\n\n";
      } else if (f instanceof Directory) {
        out += ((Directory) f).getContent() + "\n";
        for (File y : ((Directory) f).getDirectoryContents()) {
          if (y instanceof Directory) {
            String[] arg = {y.pathname};
            Ls recurr = new Ls(arg);
            out = recurr.executeRecur(out, 1);
          }
        }
      } else if (this.arguments[i].contains(">")) {
        break;
      }
    }
    return out;
  }

  /*
   * private void handleOutput(String output) { Directory root =
   * JShell.getFileSystem().getRoot(); File f = null; for (int i = 0; i <
   * this.arguments.length; i++) { if ((this.arguments[i].equals(">") ||
   * this.arguments[i].equals(">>")) && i == this.arguments.length - 2) { String
   * fullPath = Directory.relativeToAbs(this.arguments[i + 1]); if ((f =
   * root.fileAtPath(fullPath.substring(1).split("/"))) == null) { System.err
   * .println("Error! " + this.arguments[i + 1] + " is invalid path"); return; }
   * f.redirectOutput(output, this.arguments[i]); return; } if
   * ((this.arguments[i].equals(">") || this.arguments[i].equals(">>")) && (i +
   * 2) != this.arguments.length) {
   * System.err.println("Error! Usage: ls [PATHS...] [[>|>>] fileName]");
   * return; } } if (this.arguments.length >= 1) { if
   * (this.arguments[this.arguments.length - 1].equals(">") ||
   * this.arguments[this.arguments.length - 1].equals(">>")) {
   * System.err.println("Error! Usage: ls [PATHS...] [[>|>>] fileName]");
   * return; } } System.out.println(output); }
   */

  @Override
  public String redirectOutput(String output) {
    if (this.arguments.length >= 1) {
      if (this.arguments[this.arguments.length - 1].equals(">")
          || this.arguments[this.arguments.length - 1].equals(">>")) {
        return "Error! Usage: ls [PATHS...] [[>|>>] fileName]";
      }
    }
    for (int i = 0; i < this.arguments.length; i++) {
      if ((this.arguments[i].equals(">") || this.arguments[i].equals(">>"))
          && i == this.arguments.length - 2) {
        String[] args = {output, this.arguments[i], this.arguments[i + 1]};
        Echo redirect = new Echo(args);
        return redirect.execute();
      }
      if ((this.arguments[i].equals(">") || this.arguments[i].equals(">>"))
          && (i + 2) != this.arguments.length) {
        return "Error! Usage: ls [PATHS...] [[>|>>] fileName]";
      }
      if (this.arguments[i].contains(">") && (!this.arguments[i].equals(">")
          || !this.arguments[i].equals(">>"))) {
        return "Error! Usage: ls [PATHS...] [[>|>>] fileName]";
      }
    }
    JShell.setStatus();
    return output;
  }


}
