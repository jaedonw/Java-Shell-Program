// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package a2;

import java.util.Locale;
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import driver.JShell;
import java.util.Scanner;

/**
 * This class contains methods for converting text to audible speech.
 * 
 * @author Steven Pham (and Akash Sharan from geeksforgeeks.com) Website:
 *         https://www.geeksforgeeks.org/converting-text-speech-java/ Provided
 *         by Professor Abbas
 */
public class Speak extends Command {

  StringBuffer sb = new StringBuffer();

  /**
   * Constructs a Speak command object with a string array of user arguments.
   * 
   * @param arguments The array of user arguments
   */
  public Speak(String[] arguments) {
    super(arguments);
  }

  /**
   * Executes the speak command. If no arguments are entered, the function
   * continuously accepts input from the user until the keyword "QUIT" is
   * entered, and all input from the user is converted to speech. If arguments
   * are entered, the first character of the first argument and the last
   * character of the last argument must be a quotation mark ("), and the string
   * is converted to speech. The program returns an error message otherwise.
   */
  public String execute() {
    Scanner scan = new Scanner(System.in);
    if (arguments.length > 0) {
      if (arguments.length > 1) {
        singleInput();
      } else {
        if (arguments[0].charAt(0) == '\"'
            && arguments[0].charAt(arguments[0].length() - 1) == '\"') {
          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < arguments.length; i++) {
            sb.append(arguments[i] + " ");
          }
          String speech = sb.toString();
          voice(speech);
          JShell.setStatus();
        } else {
          System.out.println("usage: speak \"STRING\"");
        }
      }
    } else {
      String input = scan.nextLine();
      while (true) {
        if ((input.endsWith("QUIT") && (input.length() == 4
            || input.charAt(input.length() - 5) == ' '))) {
          input = input.substring(0, input.length() - 4);
          sb.append(input);
          break;
        }
        sb.append(input + " ");
        input = scan.nextLine();
      }
      voice(sb.toString());
      JShell.setStatus();
    }
    return null;
  }

  /**
   * Converts String s to audible speech.
   * 
   * @param s String to be converted to speech
   */
  private void voice(String s) {
    try {
      System.setProperty("freetts.voices",
          "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");

      Central.registerEngineCentral(
          "com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");

      Synthesizer synthesizer =
          Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

      synthesizer.allocate();
      synthesizer.resume();
      // converts arguments to a string

      // speaks converted string
      synthesizer.speakPlainText(s, null);
      synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY);
    }

    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Called when the user enters more than 1 argument. Appends all arguments
   * into a single string and converts to audible speech.
   */
  private void singleInput() {
    StringBuffer sb1 = new StringBuffer();
    StringBuffer sb2 = new StringBuffer();
    if (arguments[0].startsWith("\"")
        && arguments[arguments.length - 1].endsWith("\"")) {
      for (int i = 0; i < arguments.length; i++) {
        sb1.append(arguments[i]);
        sb2.append(arguments[i] + " ");
      }
      String speech = sb1.toString();
      if (speech.indexOf("\"", 1) != speech.length() - 1) {
        System.out.println("usage: speak \"STRING\"");
      } else {
        voice(sb2.toString());
      }
    } else {
      System.out.println("usage: speak \"STRING\"");
    }
  }

  /*
   * tests for this class public static void main(String args[]) {
   * System.out.println("Test 1: Empty string (free entry)"); String[] arg1 =
   * {}; Speak s1 = new Speak(arg1); s1.execute();
   * 
   * try { Thread.sleep(500); } catch (InterruptedException e) { // TODO
   * Auto-generated catch block e.printStackTrace(); }
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 2: String with no quotation marks"); String[] arg2
   * = {"hello"}; Speak s2 = new Speak(arg2); s2.execute();
   * 
   * try { Thread.sleep(500); } catch (InterruptedException e) { // TODO
   * Auto-generated catch block e.printStackTrace(); }
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 3: Single word with quotation marks"); String[]
   * arg3 = {"\"hello\""}; Speak s3 = new Speak(arg3); s3.execute();
   * 
   * try { Thread.sleep(500); } catch (InterruptedException e) { // TODO
   * Auto-generated catch block e.printStackTrace(); }
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 4: Multiple words with quotation marks"); String[]
   * arg4 = {"\"hello", "how", "are", "you\""}; Speak s4 = new Speak(arg4);
   * s4.execute();
   * 
   * try { Thread.sleep(500); } catch (InterruptedException e) { // TODO
   * Auto-generated catch block e.printStackTrace(); }
   * 
   * System.out.println("---------------------------");
   * 
   * System.out.println("Test 5: 2 string check"); String[] arg5 = {"\"hello\"",
   * "how", "are", "you\""}; Speak s5 = new Speak(arg5); s5.execute(); }
   */
}
