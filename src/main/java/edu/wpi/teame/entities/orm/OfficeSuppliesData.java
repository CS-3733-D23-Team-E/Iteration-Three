package edu.wpi.teame.entities.orm;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class OfficeSuppliesData extends ServiceRequestData {

  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String officeSupply;
  @Getter @Setter private String notes;
  @Getter @Setter private String quantity;

  public OfficeSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String officeSupply,
      String q,
      String notes) {
    super(requestID, RequestType.OFFICESUPPLIESDELIVERY, Status.PENDING, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.officeSupply = officeSupply;
    this.quantity = q;
    this.notes = notes;
  }

  public OfficeSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String officeSupply,
      String q,
      String notes,
      Status status) {
    super(requestID, RequestType.OFFICESUPPLIESDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.officeSupply = officeSupply;
    this.quantity = q;
    this.notes = notes;
  }

  public String getTable() {
    return "OfficeSupplies";
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
    if (changes.containsKey("officeSupply")) {
      this.officeSupply = changes.get("officeSupply");
    }
    if (changes.containsKey("quantity")) {
      this.quantity = changes.get("quantity");
    }
    if (changes.containsKey("notes")) {
      this.notes = changes.get("notes");
    }
  }
}
