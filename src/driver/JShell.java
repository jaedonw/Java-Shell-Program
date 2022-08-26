// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: wongjaed
// UT Student #: 1006172810
// Author: Jaedon Tyler Wong
//
// Student2:
// UTORID user_name: patelau1
// UT Student #: 1006017631
// Author: Aum Patel
//
// Student3:
// UTORID user_name: yuanwins
// UT Student #: 1006414051
// Author: Winson Yuan
//
// Student4:
// UTORID user_name: phamste5
// UT Student #: 1005901945
// Author: Steven Thai Khang Pham
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package driver;

import java.util.*;
import java.util.Scanner;
import a2.Command;
import a2.History;
import a2.Interpreter;
import a2.Directory;
import a2.FileSystem;

public class JShell {

  private static FileSystem fileSystem;
  private static History hc;
  // set to false if first command has not been executed
  private static boolean status = false;

  public static void main(String[] args) {
    fileSystem = FileSystem.getFileSystemInstance();
    Scanner input = new Scanner(System.in);
    hc = new History(); // History object to keep track of input

    while (true) {
      System.out.print("/#: "); // command prompt

      // record user input
      String userInput = input.nextLine();
      // split user input word by word and store in an array
      String[] splitInput = userInput.trim().replaceAll(" +", " ").split(" ");
      // record the command name
      String commandName = splitInput[0];
      // record the arguments
      String[] arguments = Arrays.copyOfRange(splitInput, 1, splitInput.length);
      // Adds to history no matter the validity of input
      hc.addHistory(commandName, arguments);
      // interpret the command
      Interpreter interpret =
          new Interpreter(userInput, commandName, arguments);
      Command command = interpret.getCommand();
      // check for immediate errors (to be completed later)
      // execute the command
      String returnValue;
      if (command != null) {
        returnValue = command.execute();
        if (returnValue != null) {
          System.out.println(returnValue);
        }
      } else if (commandName.equals("history")) {
        History temp = new History(arguments, hc);
        returnValue = temp.execute();
        if (returnValue != null) {
          System.out.println(returnValue);
        }
      }
    }
  }

  public static FileSystem getFileSystem() {
    return fileSystem;
  }

  public static History getCommandHistory() {
    return hc;
  }

  public static void setFileSystem(FileSystem f) {
    fileSystem = f;
  }

  public static void setCommandHistory(History h) {
    hc = h;
  }

  public static boolean getStatus() {
    return status;
  }

  public static String setStatus() {
    status = true;
    return null;
  }
}
