package org.legomin.elevatorsimulation.elevator.elevatorStrategyImpl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.legomin.elevatorsimulation.elevator.Direction;
import org.legomin.elevatorsimulation.elevator.ElevatorStrategy;
import org.legomin.elevatorsimulation.elevator.exception.NotSupportedTaskException;

/**
 * Just mock strategy
 *
 * support commands:
 *  int numbers (0 <= <floor_number> <= <floor_count>)
 *
 *  !! Not sensable to inside/outside elevator command
 */
public class MockElevatorStrategy implements ElevatorStrategy {

  private final Queue<Integer> tasks;
  private final int floorCount;

  public MockElevatorStrategy(final int floorCount) {
    this.floorCount = floorCount;
    this.tasks = new ConcurrentLinkedQueue<>();
  }

  @Override
  public int getTask(int floor, Direction direction) {
    if (tasks.isEmpty()) {
      return 0;
    }
    return tasks.poll();
  }

  @Override
  public void addTask(String task) {
    try {
      int intTask = Integer.parseInt(task);
      if (intTask > floorCount | intTask <= 0) {
        throw new NotSupportedTaskException("Incorrect command: " + task + " should be between 1 & " + floorCount);
      }
      tasks.add(intTask);
    } catch (NumberFormatException e) {
      throw new NotSupportedTaskException("Incorrect command: " + task + " should be between 1 & " + floorCount);
    }
  }

  @Override
  public boolean needToStopHere(int floor, Direction direction) {
    return false;
  }

}
