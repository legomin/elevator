package org.legomin.elevatorsimulation.elevator;

import org.legomin.elevatorsimulation.elevator.exception.NotSupportedTaskException;

/**
 * Interface for elevator behavior strategy pattern
 *
 * Storage command format for elevator consumers (command producers)))),
 * commands list & elevator behavior
 *
 */
public interface ElevatorStrategy {

  /**
   * get next task to elevator runnable
   *
   * @param floor - current elevator floor
   * @param direction - current elevator diraction
   * @return - -1 if command queue is empty, next command otherwise
   */
  int getTask(int floor, Direction direction);

  /**
   * helps elevator optimize tasks, execute some additional tasks during executing main
   *  f.e. get additional people by the way to first floor )
   *
   * @param floor - current floor
   * @param direction - current direction
   * @return - if true, elevator runnable opens door at that floor
   */
  boolean needToStopHere(int floor, Direction direction);

  /**
   * parse task & save it to inner queue
   *
   * @param task - string task
   * @throws NotSupportedTaskException - throws if command isn't be parsed or not supported
   */
  void addTask(String task) throws NotSupportedTaskException;

}
