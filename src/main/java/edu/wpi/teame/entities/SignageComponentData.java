package edu.wpi.teame.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.Setter;

public class SignageComponentData {
  public enum ArrowDirections {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    STOP_HERE;

    public static String directionToString(SignageComponentData.ArrowDirections st) {
      switch (st) {
        case UP:
          return "UP";
        case DOWN:
          return "DOWN";
        case LEFT:
          return "LEFT";
        case RIGHT:
          return "RIGHT";
        case STOP_HERE:
          return "STOP_HERE";
        default:
          throw new NoSuchElementException("No such direction found");
      }
    }

    public static SignageComponentData.ArrowDirections stringToDirection(String st) {
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

    public static List<ArrowDirections> getAllDirections() {
      ArrayList<ArrowDirections> dirList = new ArrayList<>();
      dirList.add(UP);
      dirList.add(DOWN);
      dirList.add(LEFT);
      dirList.add(RIGHT);
      dirList.add(STOP_HERE);
      return dirList;
    }
  }

  @Getter @Setter private String locationNames;
  @Getter @Setter private String kiosk_location;
  @Getter @Setter private ArrowDirections arrowDirections;
  @Getter @Setter private String date;

  public SignageComponentData(
      String date, String kiosk_location, String locationNames, ArrowDirections arrowDirections) {
    this.date = date;
    this.kiosk_location = kiosk_location;
    this.locationNames = locationNames;
    this.arrowDirections = arrowDirections;
  }
}
