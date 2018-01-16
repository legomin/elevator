package org.legomin.elevatorsimulation.elevator.elevatorStrategyImpl;

import static org.legomin.elevatorsimulation.elevator.Direction.DOWN;
import static org.legomin.elevatorsimulation.elevator.Direction.UP;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.legomin.elevatorsimulation.elevator.Direction;
import org.legomin.elevatorsimulation.elevator.ElevatorStrategy;
import org.legomin.elevatorsimulation.elevator.exception.NotSupportedTaskException;

/**
 * Standard living house elevator implementation:
 * supports only one button on every floor.
 *   the first floor means "Want up", others - "want to the first floor"
 *
 * inside elevator pushed buttons have higher priority,
 *  elevator executes them first
 *
 *  command format: 'i'|'o'(1-<floor_count>)
 *  i - pushed button inside elevator
 *  o - pushed button at a floor
 */
public class LivingHouseElevatorStrategy implements ElevatorStrategy {

  private static final char INSIDE_PREFIX = 'i';
  private static final char OUTSIDE_PREFIX = 'o';

  private final NavigableSet<Integer> inside = new ConcurrentSkipListSet<>();
  private final NavigableSet<Integer> outside = new ConcurrentSkipListSet<>();
  private final int floorCount;

  public LivingHouseElevatorStrategy(final int floorCount) {
    this.floorCount = floorCount;
  }

  @Override
  public int getTask(int floor, Direction direction) {
    if (!inside.isEmpty()) {
      if (direction == UP) {
        return inside.pollLast();
      } else {
        return inside.pollFirst();
      }
    } else if (!outside.isEmpty()) {
      return outside.pollLast();
    } else {
      return -1;
    }
  }

  @Override
  public void addTask(String task) {
    try {
      char source = task.charAt(0);
      int floor = Integer.parseInt(task.substring(1));
      if (floor > floorCount | floor <= 0) {
        throw new NotSupportedTaskException("Incorrect command: " + task + " should be between 1 & " + floorCount);
      }
      if (source == INSIDE_PREFIX) {
        inside.add(floor);
      } else if (source == OUTSIDE_PREFIX) {
        outside.add(floor);
      } else {
        throw new NotSupportedTaskException("Incorrect command: " + source + " should be between 'i' or 'o'");
      }
    } catch (NumberFormatException e) {
      throw new NotSupportedTaskException("Incorrect command: " + task + " should be between 1 & " + floorCount);
    }
  }

  @Override
  public boolean needToStopHere(int floor, Direction direction) {
    //if we have the task inside elevator to stop this floor - do it
    if (!inside.isEmpty()) {
      if (inside.contains(floor)) {
        inside.remove(floor);
        return true;
      }
    // if we going down, we can carry other people by the way
    } else if (direction == DOWN && !outside.isEmpty()) {
      if (outside.contains(floor)) {
        outside.remove(floor);
        return true;
      }
    }
    return false;
  }

}
