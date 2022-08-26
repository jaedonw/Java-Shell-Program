package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import a2.Exit;

public class ExitTest {

  Exit e;
  MockFileSystem mfs;

  @Before
  public void setUp() throws Exception {
    mfs = new MockFileSystem();
  }

  // constructor with no arguments
  @Test
  public void constructorTest1() {
    String arguments[] = {};
    e = new Exit(arguments);
    assertTrue(e.getArguments().equals(arguments));
  }

  // constructor with arguments
  @Test
  public void constructorTest2() {
    String arguments[] = {"1", "2"};
    e = new Exit(arguments);
    assertTrue(e.getArguments().equals(arguments));
  }

}
