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
 * Find implementation.
 * 
 * @author Winson Yuan
 */
public class Find extends Command implements OutputRedirection {

  private String fileName;
  private String absolutePath;
  private String[] pathnameArray;
  private String content;
  private boolean redirect = false;
  private int max;
  private ErrorChecker error;

  /**
   * Constructs a Find command object from string array of arguments.
   * 
   * @param arguments The input from user in a string array
   */
  public Find(String[] arguments) {
    super(arguments);
    max = arguments.length;
    content = "";
    if (arguments.length >= 5) {
      if (arguments[max - 2].equals(">") || arguments[max - 2].equals(">>")) {
        redirect = true;
        if (arguments[max - 1].contains(".")
            || arguments[max - 1].contains("..")) {
          arguments[max - 1] =
              Interpreter.interpretPathname(arguments[max - 1]);
        }
        if (!(arguments[max - 1].startsWith("/"))) {
          absolutePath = Directory.relativeToAbs(arguments[max - 1]);
        } else {
          absolutePath = arguments[max - 1];
        }
        pathnameArray = absolutePath.split("/");
        if (pathnameArray.length != 0) {
          fileName = pathnameArray[pathnameArray.length - 1];
        }
        max = max - 2;
      }
    }
  }

  private boolean checkErrors() {
    if (max < 5) {
      return true;
    }
    if (!(arguments[max - 2].equals("-name"))) {
      return true;
    }
    if (!((arguments[max - 3].equals("f"))
        || (arguments[max - 3].equals("d")))) {
      return true;
    }
    if (!(arguments[max - 4].equals("-type"))) {
      return true;
    }
    if (!(arguments[max - 1].charAt(0) == '"'
        && arguments[max - 1].charAt(arguments[max - 1].length() - 1) == '"')) {
      return true;
    }
    if (redirect) {
      if (!error.checkValidFileName(this.fileName)) {
        return true;
      }
      if (arguments[max + 1].contains("//")) {
        System.out.println("error: pathname is invalid");
        return true;
      }
    }
    arguments[max - 1] =
        arguments[max - 1].substring(1, arguments[max - 1].length() - 1);
    return false;
  }

  private void search(File test, String name, String type) {
    LinkedList<File> queue = new LinkedList<File>();
    queue.add(test);
    while (!queue.isEmpty()) {
      File current = queue.remove();
      if (type.equals("d")) {
        if (current.fileName.equals(name) && current instanceof Directory) {
          content = content + "Found directory in " + current.pathname + "\n";
        }
      } else if (type.equals("f")) {
        if (current.fileName.equals(name) && !(current instanceof Directory)) {
          content = content + "Found file in " + current.pathname + "\n";
        }
      }
      if (current instanceof Directory) {
        queue.addAll(((Directory) current).getDirectoryContents());
      }
    }
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
          if (arguments[max].equals(">")) {
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
   * Checks that the input is syntactically correct, searches the path and all
   * it's sub-directories/sub-files if there exist a directory/file with a
   * certain name
   */
  public String execute() {
    this.error = new ErrorChecker();
    if (checkErrors()) {
      return ("find: Usage `find [path ...] -type [f|d] -name expression`");
    }
    for (int i = 0; i < max - 4; i++) {
      if (arguments[i].contains(".") || arguments[i].contains("..")) {
        arguments[i] = Interpreter.interpretPathname(arguments[i]);
      }
      if (!arguments[i].startsWith("/")) {
        arguments[i] = Directory.relativeToAbs(arguments[i]);
      }
      File base = File.getFile(arguments[i].split("/"), arguments[i]);
      if (base == null) {
        System.out.println("find: " + arguments[i] + " path not found");
      } else {
        search(base, arguments[max - 1], arguments[max - 3]);
      }
    }
    return redirectOutput(this.content);
  }
}
