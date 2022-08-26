package a2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import driver.JShell;
import a2.File;

/**
 * This class contains methods for retrieving HTML text from a URL and storing
 * it in a file in the file system.
 * 
 * @author Steven Pham
 *
 */
public class Curl extends Command {

  private ErrorChecker error;

  /**
   * Constructs a Curl command object with a string array of user arguments.
   * 
   * @param arguments The array of user arguments
   */
  public Curl(String[] arguments) {
    super(arguments);
    error = new ErrorChecker();
  }

  /**
   * Executes the curl command. If the URL exists, its text will be extracted
   * and stored in a file in the file system. If the URL does not exist, the
   * function will return an error message.
   */
  public String execute() {
    if (!error.checkExcessArguments(this, 1))
      ;
    else if (!error.checkMissingArguments(this, 1))
      ;
    else {
      try {
        URL link = new URL(arguments[0]);
        if (!getExtension().equals(".txt") && !getExtension().equals(".html")) {
          return "incorrect filetype";
        }
        BufferedReader br =
            new BufferedReader(new InputStreamReader(link.openStream()));
        String line;
        StringBuffer sb = new StringBuffer();
        line = br.readLine();
        while (line != null) {
          sb.append(line + "\n");
          line = br.readLine();
        }
        br.close();
        File f = File.getFile(getName().split("/"), getName());
        if (f == null) {
          f = new File(getName(), sb.toString(),
              JShell.getFileSystem().getCurrentDir().pathname + getName(),
              JShell.getFileSystem().getCurrentDir());
          JShell.getFileSystem().getCurrentDir().addContent(f);
        } else {
          f.setContent(sb.toString());
        }
        return JShell.setStatus();
      } catch (Exception e) {
        return "invalid URL";
      }
    }
    return null;
  }

  private String getName() {
    int slashIndex = -1;
    for (int i = 0; i < arguments[0].length(); i++) {
      if (arguments[0].charAt(i) == '/') {
        slashIndex = i;
      }
    }

    String fileName =
        arguments[0].substring(slashIndex + 1, arguments[0].length());
    int dotIndex = fileName.indexOf('.');
    if (dotIndex != -1) {
      fileName = fileName.substring(0, dotIndex);
    }
    return fileName;
  }

  private String getExtension() {
    int slashIndex = -1;
    for (int i = 0; i < arguments[0].length(); i++) {
      if (arguments[0].charAt(i) == '/') {
        slashIndex = i;
      }
    }

    String fileName =
        arguments[0].substring(slashIndex + 1, arguments[0].length());
    String extension = null;
    int dotIndex = fileName.indexOf('.');
    if (dotIndex != -1) {
      extension = fileName.substring(dotIndex, fileName.length());
    }
    return extension;
  }

  /**
   * @return The name of the command ("curl")
   */
  public String toString() {
    return "curl";
  }

}
