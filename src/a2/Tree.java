// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.LinkedList;
import driver.JShell;

/**
 * Tree implementation.
 * 
 * @author Winson Yuan
 */
public class Tree extends Command implements OutputRedirection {

  private String fileName;
  private String absolutePath;
  private String[] pathnameArray;
  private String tree;
  private boolean redirect = false;

  /**
   * Constructs a tree command object from string array of arguments.
   * 
   * @param arguments The input from user in a string array
   */
  public Tree(String[] arguments) {
    super(arguments);
    tree = "";
    if (arguments.length == 2) {
      if (arguments[0].equals(">") || arguments[0].equals(">>")) {
        redirect = true;
      }
      if (arguments[1].contains(".") || arguments[1].contains("..")) {
        arguments[1] = Interpreter.interpretPathname(arguments[1]);
      }
      if (!arguments[1].startsWith("/")) {
        absolutePath = Directory.relativeToAbs(arguments[1]);
      } else {
        absolutePath = arguments[1];
      }
      pathnameArray = absolutePath.split("/");
      if (pathnameArray.length != 0) {
        fileName = pathnameArray[pathnameArray.length - 1];
      }
    }
  }

  /**
   * String representation of Tree object
   * 
   * @return String representing tree command
   */
  public String toString() {
    return "tree";
  }

  private boolean checkErrors() {
    ErrorChecker error = new ErrorChecker();
    if (arguments.length == 2) {
      if (redirect) {
        if (!error.checkValidFileName(this.fileName)) {
          return true;
        } else if (!error.checkIfPathCanBeMade(arguments[1])) {
          return true;
        } else if (arguments[1].contains("//")) {
          System.out.println("error with pathname");
          return true;
        }
        return false;
      }
    }
    if (!error.checkNumArguments(this, 0)) {
      return true;
    }
    return false;
  }

  @Override
  public String redirectOutput(String output) {
    if (redirect) {
      File foundFile = File.getFile(pathnameArray, absolutePath);
      if (foundFile == null) {
        Directory parentDir = Directory.getParentDir(absolutePath);
        if (parentDir == null)
          return null;
        File add = new File(fileName, output, absolutePath, parentDir);
        parentDir.addContent(add);
      } else {
        if (!(foundFile instanceof Directory)) {
          if (arguments[0].equals(">")) {
            foundFile.setContent(output);
          } else {
            foundFile.setContent(foundFile.getContent() + output);
          }
        } else {
          return ("Error: path is a directory");
        }
      }
      return JShell.setStatus();
    }
    JShell.setStatus();
    return output;
  }

  /**
   * Executes the tree command, if user inputs any arguments, send an error.
   * Displays the entire file system as a tree
   */
  public String execute() {
    if (checkErrors()) {
      return null;
    }
    LinkedList<File> stack = new LinkedList<File>();
    stack.addFirst(JShell.getFileSystem().getRoot());
    while (!stack.isEmpty()) {
      File current = stack.remove();
      String spacing = "";
      if (current.equals(JShell.getFileSystem().getRoot())) {
        tree = "/" + "\n";
      } else {
        for (int i = 0; i < current.pathname.length(); i++) {
          if (current.pathname.charAt(i) == '/') {
            spacing = spacing + "  ";
          }
        }
        tree = tree + spacing + current.fileName + "\n";
      }
      if (current instanceof Directory) {
        for (int j = 0; j < ((Directory) current).sizeOfContents(); j++) {
          stack.addFirst(((Directory) current).getAtIndex(j));
        }
      }
    }
    return redirectOutput(tree);
  }
}
