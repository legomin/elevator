package org.legomin.elevatorsimulation.elevator;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ElevatorTest {

  private static final PrintStream oldStream = System.out;
  private static final ByteArrayOutputStream bos = new ByteArrayOutputStream();
  private static final PrintStream printStream = new PrintStream(bos, true);

  private Elevator elevator;

  @Before
  public void setUp() throws Exception {
    System.setOut(printStream);
    elevator = new Elevator(20, 1, 1, 1, ElevatorType.LIVING);
  }

  @After
  public void tearDown() throws Exception {
    System.setOut(oldStream);
  }

  @Test
  public void testElevatorWork() throws Exception {
    elevator.start();
    elevator.addTask("i1");
    elevator.addTask("i7");
    elevator.addTask("o5");
    elevator.addTask("o3");

    Thread.sleep(16000);
    assertEquals("Unexpected value", "elevator started\n" + "Floor: 1, moved UP\n" +
      "Floor: 1, doors are opened\n" + "Floor: 1, doors are closed\n" + "Floor: 2, moved UP\n" +
      "Floor: 3, moved UP\n" + "Floor: 4, moved UP\n" + "Floor: 5, moved UP\n" + "Floor: 6, moved UP\n" +
      "Floor: 7, moved UP\n" + "Floor: 7, doors are opened\n" + "Floor: 7, doors are closed\n" +
      "Floor: 6, moved DOWN\n" + "Floor: 5, moved DOWN\n" + "Floor: 5, doors are opened\n" +
      "Floor: 5, doors are closed\n" + "Floor: 4, moved DOWN\n" + "Floor: 3, moved DOWN\n" +
      "Floor: 3, doors are opened\n" + "Floor: 3, doors are closed\n", bos.toString());

    elevator.stop();
  }


}