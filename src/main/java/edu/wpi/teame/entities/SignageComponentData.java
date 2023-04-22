package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.NoSuchElementException;

import static javax.swing.UIManager.getString;

public class SignageComponentData {
    public enum Directions {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        STOP_HERE;

        public static String statusToString(SignageComponentData.Directions st) {
            return getString(st);
        }

        public static SignageComponentData.Directions stringToDirection(String st) {
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
    @Getter @Setter private Directions directions;
    @Getter @Setter private String date;

    public SignageComponentData(String locationNames, Directions directions, String date){
        this.locationNames = locationNames;
        this.directions = directions;
        this.date = date;
    }
}
