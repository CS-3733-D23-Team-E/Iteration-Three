package edu.wpi.teame.entities.orm;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class FlowerRequestData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String flowerType;

  @Getter @Setter private String quantity;
  @Getter @Setter private String card;

  @Getter @Setter private String cardMessage;

  @Getter @Setter private String notes;

  public FlowerRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String flowerType,
      String quantity,
      String card,
      String cardMessage,
      String notes) {
    super(requestID, RequestType.FLOWERDELIVERY, Status.PENDING, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.flowerType = flowerType;
    this.quantity = quantity;
    this.card = card;
    this.cardMessage = cardMessage;
    this.notes = notes;
  }

  public FlowerRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String flowerType,
      String quantity,
      String card,
      String cardMessage,
      String notes,
      Status status) {
    super(requestID, RequestType.FLOWERDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.flowerType = flowerType;
    this.quantity = quantity;
    this.card = card;
    this.cardMessage = cardMessage;
    this.notes = notes;
  }

  public String getTable() {
    return "FlowerRequest";
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
    if (changes.containsKey("flowerType")) {
      this.flowerType = changes.get("flowerType");
    }
    if (changes.containsKey("quantity")) {
      this.quantity = changes.get("quantity");
    }
    if (changes.containsKey("card")) {
      this.card = changes.get("card");
    }
    if (changes.containsKey("cardMessage")) {
      this.cardMessage = changes.get("cardMessage");
    }
    if (changes.containsKey("notes")) {
      this.notes = changes.get("notes");
    }
  }
}
