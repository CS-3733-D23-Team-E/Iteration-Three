package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

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
}
