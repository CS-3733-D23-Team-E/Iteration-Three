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
        case "LEFT":
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
  @Getter @Setter private String kiosk_location;
  @Getter @Setter private arrowDirections arrowDirections;
  @Getter @Setter private String date;

  public SignageComponentData(
      String date, String kiosk_location, String locationNames, arrowDirections arrowDirections) {
    this.date = date;
    this.kiosk_location = kiosk_location;
    this.locationNames = locationNames;
    this.arrowDirections = arrowDirections;
  }
}
