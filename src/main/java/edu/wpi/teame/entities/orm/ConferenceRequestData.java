package edu.wpi.teame.entities.orm;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class ConferenceRequestData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String roomRequest;
  @Getter @Setter private String notes;

  public ConferenceRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String roomRequest,
      String notes) {
    super(requestID, RequestType.CONFERENCEROOM, Status.PENDING, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.roomRequest = roomRequest;
    this.notes = notes;
  }

  public ConferenceRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String roomRequest,
      String notes,
      Status status) {
    super(requestID, RequestType.CONFERENCEROOM, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.roomRequest = roomRequest;
    this.notes = notes;
  }

  public String getTable() {
    return "ConferenceRequest";
  }

  public void applyChanges(HashMap<String, String> changes) {
    super.applyChanges(changes);
    if (changes.containsKey("name")) {
      this.name = changes.get("name");
    }
    if (changes.containsKey("room")) {
      this.room = changes.get("room");
    }
    if (changes.containsKey("deliveryDate")) {
      this.deliveryDate = changes.get("deliveryDate");
    }
    if (changes.containsKey("deliveryTime")) {
      this.deliveryTime = changes.get("deliveryTime");
    }
    if (changes.containsKey("roomRequest")) {
      this.roomRequest = changes.get("roomRequest");
    }
    if (changes.containsKey("notes")) {
      this.notes = changes.get("notes");
    }
  }
}
