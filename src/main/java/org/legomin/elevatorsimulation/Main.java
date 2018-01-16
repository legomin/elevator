package org.legomin.elevatorsimulation;

import java.io.InvalidObjectException;
import java.util.Scanner;

import org.legomin.elevatorsimulation.elevator.Elevator;
import org.legomin.elevatorsimulation.elevator.ElevatorType;

public class Main {

  private static final int MIN_FLOOR_COUNT = 5;
  private static final int MAX_FLOOR_COUNT = 20;

  public static void main(String[] args) {

    try {
      final int[] intArgs = parseArgs(args);
      validateArgs(intArgs);

      final Elevator lift = new Elevator(intArgs[0], intArgs[1], intArgs[2], intArgs[3], ElevatorType.LIVING);

      lift.start();
      Scanner scanner = new Scanner(System.in);
      while (true) {
        final String input = scanner.nextLine();
        if (input.equals("q")) {
          lift.stop();
          break;
        }
        lift.addTask(input);
      }
    } catch (NumberFormatException|IndexOutOfBoundsException e) {
      System.out.println("Program args [0..3] should be int numbers > 0");
    } catch (InvalidObjectException e) {
      System.out.println(e.getMessage());
    }
  }

  private static int[] parseArgs(String[] args) {
    final int[] result = new int[4];
    for (int i = 0; i < 4; i++) {
      result[i] = Integer.parseInt(args[i]);
    }
    return result;
  }

  private static void validateArgs(final int[] args) throws InvalidObjectException {
    if (args[0] < MIN_FLOOR_COUNT | args[0] > MAX_FLOOR_COUNT) {
      throw new InvalidObjectException("The floor count shoud be between 5 & 20");
    }
  }

}
