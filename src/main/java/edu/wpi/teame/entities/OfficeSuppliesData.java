package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

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
}
