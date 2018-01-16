package org.legomin.elevatorsimulation.elevator;

import static java.util.Objects.requireNonNull;

import org.legomin.elevatorsimulation.elevator.elevatorStrategyImpl.LivingHouseElevatorStrategy;
import org.legomin.elevatorsimulation.elevator.elevatorStrategyImpl.MockElevatorStrategy;
import org.legomin.elevatorsimulation.elevator.exception.NotSupportedTaskException;

/**
 * Main elevator class
 */
public class Elevator {

  private final Thread liftThread;
  private final ElevatorStrategy strategy;
  private final Object monitor = new Object();

  private Elevator(int floorCount, int floorHighMeter, int speedMetersBySecond, int doorOpenTimeSeconds,
    ElevatorStrategy strategy) {
    validateArgs(floorCount, floorHighMeter, speedMetersBySecond, doorOpenTimeSeconds);
    this.strategy = requireNonNull(strategy);
    this.liftThread = new Thread(new ElevatorThread((floorHighMeter * 1000) / speedMetersBySecond,
      doorOpenTimeSeconds * 1000, strategy, monitor));
  }

  public Elevator(int floorCount, int floorHighMeter, int speedMetersBySecond, int doorOpenTimeSeconds,
    ElevatorType elevatorType) {
    this(floorCount, floorHighMeter, speedMetersBySecond, doorOpenTimeSeconds, getStrategy(elevatorType, floorCount));
  }

  public void start() {
    liftThread.start();
    System.out.println("elevator started");
  }

  public void stop() {
    liftThread.interrupt();
    System.out.println("elevator stopped");
  }

  public void addTask(String task) {
    try{
      strategy.addTask(task);
      synchronized (monitor) {
        monitor.notify();
      }
    } catch (NotSupportedTaskException e) {
      System.out.println("Task: " + task + " skipped, cause: " + e.getLocalizedMessage());
    }
  }

  private static ElevatorStrategy getStrategy(ElevatorType type, int floorCount) {
    if (type == ElevatorType.MOCK) {
      return new MockElevatorStrategy(floorCount);
    } else if (type == ElevatorType.LIVING) {
      return new LivingHouseElevatorStrategy(floorCount);
    }
    throw new RuntimeException("Unsupported elevator type: " + type);
  }

  private static void validateArgs(int floorCount, int floorHighMeter, int speedMetersBySecond,
    int doorOpenTimeSeconds) {
    if (floorCount <= 0 | floorHighMeter <= 0 | speedMetersBySecond <= 0 | doorOpenTimeSeconds <= 0) {
      throw new RuntimeException("Unsupported constructor parameters");
    }
  }

}
