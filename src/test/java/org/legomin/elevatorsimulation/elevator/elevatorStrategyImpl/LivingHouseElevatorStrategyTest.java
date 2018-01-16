package org.legomin.elevatorsimulation.elevator.elevatorStrategyImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.legomin.elevatorsimulation.elevator.Direction.UP;

import org.junit.Test;
import org.legomin.elevatorsimulation.elevator.ElevatorStrategy;
import org.legomin.elevatorsimulation.elevator.exception.NotSupportedTaskException;

public class LivingHouseElevatorStrategyTest {

  private final ElevatorStrategy strategy = new LivingHouseElevatorStrategy(20);

  @Test
  public void testQueueWork() throws Exception {
    strategy.addTask("i1");
    strategy.addTask("i7");
    strategy.addTask("i10");
    strategy.addTask("i20");

    strategy.addTask("o5");
    strategy.addTask("o3");

    assertEquals("Unexpected value", 20, strategy.getTask(1, UP));
    assertTrue("Expected true", strategy.needToStopHere(1, UP));
    assertFalse("Expected false", strategy.needToStopHere(2, UP));
    assertTrue("Expected true", strategy.needToStopHere(7, UP));
    assertFalse("Expected false",strategy.needToStopHere(9, UP));
    assertTrue("Expected true", strategy.needToStopHere(10, UP));
    assertEquals("Unexpected value", 5, strategy.getTask(20, UP));
    assertEquals("Unexpected value", 3, strategy.getTask(5, UP));

  }

  @Test(expected = NotSupportedTaskException.class)
  public void testWrongCommandFail() throws Exception {
    strategy.addTask("w2");
  }

  @Test(expected = NotSupportedTaskException.class)
  public void testWrongFloorFail() throws Exception {
    strategy.addTask("o21");
  }

}