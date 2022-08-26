// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import a2.Speak;

public class SpeakTest {

  MockFileSystem mfs;
  Speak s;

  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
  }

  // constructor with no arguments
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    s = new Speak(arguments);
    assertTrue(s.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    s = new Speak(arguments);
    assertTrue(s.getArguments().equals(arguments));
  }

  // execute with proper quotation marks
  @Test
  public void executeTest1() {
    String arguments[] = {"\"this", "is", "a", "sentence\""};
    Speak s1 = new Speak(arguments);
    assertEquals(s1.execute(), null);
  }

  // execute without proper quotation marks
  @Test
  public void executeTest2() {
    String arguments[] = {"this", "is", "a", "sentence"};
    Speak s1 = new Speak(arguments);
    assertEquals(s1.execute(), null);
  }
}
