package edu.wpi.teame.entities;

import static javax.swing.UIManager.getString;

import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.Setter;

public class SignageComponentData {
  public enum arrowDirections {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    STOP_HERE;

    public static String directionToString(SignageComponentData.arrowDirections st) {
      return getString(st);
    }

    public static SignageComponentData.arrowDirections stringToDirection(String st) {
      switch (st) {
        case "UP":
          return UP;
        case "DOWN":
          return DOWN;
        case "DONE":
          return LEFT;
        case "RIGHT":
          return RIGHT;
        case "STOP_HERE":
          return STOP_HERE;
        default:
          throw new NoSuchElementException("No such direction found");
      }
    }
  }

  @Getter @Setter private String locationNames;
  @Getter @Setter private arrowDirections directions;
  @Getter @Setter private String date;

  public SignageComponentData(String date, String locationNames, arrowDirections directions) {
    this.locationNames = locationNames;
    this.directions = directions;
    this.date = date;
  }
}
