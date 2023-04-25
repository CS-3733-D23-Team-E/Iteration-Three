package edu.wpi.teame.entities;

import static javax.swing.UIManager.getString;

import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.Setter;

public abstract class ServiceRequestData {
  public enum Status {
    PENDING,
    IN_PROGRESS,
    DONE;

    public static String statusToString(Status st) {
      return getString(st);
    }

    public static Status stringToStatus(String st) {
      switch (st) {
        case "PENDING":
          return PENDING;
        case "IN_PROGRESS":
          return IN_PROGRESS;
        case "DONE":
          return DONE;
        default:
          throw new NoSuchElementException("No such status found");
      }
    }
  }

  public enum RequestType {
    MEALDELIVERY,
    FLOWERDELIVERY,
    OFFICESUPPLIESDELIVERY,
    FURNITUREDELIVERY,
    CONFERENCEROOM,
    MEDICALSUPPLIESDELIVERY;


    public static RequestType stringToRequestType(String rt) {
      switch (rt) {
        case "MEALDELIVERY":
          return MEALDELIVERY;
        case "FLOWERDELIVERY":
          return FLOWERDELIVERY;
        case "OFFICESUPPLIES":
          return OFFICESUPPLIESDELIVERY;
        case "CONFERENCEROOM":
          return CONFERENCEROOM;
        case " MEDICALSUPPLIESDELIVERY":
          return MEDICALSUPPLIESDELIVERY;

        default:
          throw new NoSuchElementException("No such service request found");
      }
    }

    public static String requestTypeToString(RequestType rt) {
      switch (rt) {
        case MEALDELIVERY:
          return "MEALDELIVERY";
        case FLOWERDELIVERY:
          return "FLOWERDELIVERY";
        case OFFICESUPPLIESDELIVERY:
          return "OFFICESUPPLIES";
        case CONFERENCEROOM:
          return "CONFERENCEROOM";
        case MEDICALSUPPLIESDELIVERY:
          return "MEDICALSUPPLIESDELIVERY";
        default:
          throw new NoSuchElementException("No such service request found");
      }
    }
  }

  @Getter @Setter private int requestID;
  @Getter @Setter private RequestType requestType;
  @Getter @Setter private Status requestStatus;
  @Getter @Setter private String assignedStaff;

  public ServiceRequestData(
      int requestID, RequestType requestType, Status requestStatus, String assignedStaff) {
    this.requestID = requestID;
    this.requestType = requestType;
    this.requestStatus = requestStatus;
    this.assignedStaff = assignedStaff;
  }
}
