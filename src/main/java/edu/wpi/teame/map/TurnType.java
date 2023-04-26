package edu.wpi.teame.map;

public enum TurnType {
  RIGHT("turn right."),
  LEFT("turn left."),
  STRAIGHT("continue straight."),
  START("start"),
  END("end"),
  ELEVATOR("take the elevator."),
  STAIRS("take the stairs."),
  ERROR("ERROR");

  private final String turnString;

  TurnType(String turnString) {
    this.turnString = turnString;
  }

  public String getTurnString() {
    return turnString;
  }
}
