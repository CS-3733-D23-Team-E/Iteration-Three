package edu.wpi.teame.entities.orm;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class FurnitureRequestData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String furnitureType;

  @Getter @Setter private String notes;

  public FurnitureRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String furnitureType,
      String notes) {
    super(requestID, RequestType.FURNITUREDELIVERY, Status.PENDING, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.furnitureType = furnitureType;
    this.notes = notes;
  }

  public FurnitureRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String furnitureType,
      String notes,
      Status status) {
    super(requestID, RequestType.FURNITUREDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.furnitureType = furnitureType;
    this.notes = notes;
  }

  public String getTable(){
    return "FurnitureRequest";
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
    if (changes.containsKey("furnitureType")) {
      this.furnitureType = changes.get("furnitureType");
    }
    if (changes.containsKey("notes")) {
      this.notes = changes.get("notes");
    }
  }
}
