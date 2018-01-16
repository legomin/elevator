package org.legomin.elevatorsimulation.elevator;

import static org.legomin.elevatorsimulation.elevator.Direction.DOWN;
import static org.legomin.elevatorsimulation.elevator.Direction.UP;

/**
 * Runnable elevator component, statefull
 *
 * invoke commands only like "go to N floor & open doors",
 * independently either command was inside the elevator or from floor
 * that's why commands are just number floor integers
 *
 */
public class ElevatorThread implements Runnable {

  private final int floorChangeTimeMs;
  private final int doorOpenTimeMs;
  private final ElevatorStrategy strategy;
  private final Object monitor;

  //State fields
  private int currentFloor = 0;
  private Direction currentDirection = UP;

  ElevatorThread(int floorChangeTimeMs, int doorOpenTimeMs, ElevatorStrategy strategy, Object monitor) {
    this.floorChangeTimeMs = floorChangeTimeMs;
    this.doorOpenTimeMs = doorOpenTimeMs;
    this.strategy = strategy;
    this.monitor = monitor;
  }

  @Override
  public void run() {
    try {
      while (!Thread.interrupted()) {
        final int task = strategy.getTask(currentFloor, currentDirection);

        if (task == -1) {
          synchronized (monitor) {
            monitor.wait();
          }
        } else if (task == currentFloor) {
          openDoors();
        } else {
          final Direction direction;
          final int countFloorsToMove;
          if (task > currentFloor) {
            direction = UP;
            countFloorsToMove = task - currentFloor;
          } else {
            direction = DOWN;
            countFloorsToMove = -task + currentFloor;
          }
          for (int i = 0; i < countFloorsToMove; i++) {
            moveFloor(direction);
          }
          openDoors();
        }
      }
    } catch (InterruptedException e) {
      System.out.println("bye");
    }
  }

  private void openDoors() throws InterruptedException {
    System.out.println("Floor: " + currentFloor + ", doors are opened");
    Thread.sleep(doorOpenTimeMs);
    System.out.println("Floor: " + currentFloor + ", doors are closed");
  }

  private void moveFloor(final Direction direction) throws InterruptedException {
    Thread.sleep(floorChangeTimeMs);
    if (direction == UP) {
      changeState(++currentFloor, UP);
    } else {
      changeState(--currentFloor, DOWN);
    }
    if (strategy.needToStopHere(currentFloor, currentDirection)) {
      openDoors();
    }
  }

  private void changeState(final int floor, final Direction direction) {
    currentFloor = floor;
    currentDirection = direction;
    System.out.println("Floor: " + floor + ", moved " + direction);
  }

}
