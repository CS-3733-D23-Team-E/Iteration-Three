package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class MedicalSuppliesData extends ServiceRequestData {

  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String medicalSupply;
  @Getter @Setter private String notes;
  @Getter @Setter private String quantity;
  /*@Getter @Setter private String quantity;
  @Getter @Setter private String quantity;
  @Getter @Setter private String quantity;
  @Getter @Setter private String quantity;
  @Getter @Setter private String quantity;
  @Getter @Setter private String quantity;*/

  public MedicalSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String medicalSupply,
      String q,
      String notes) {
    super(requestID, RequestType.MEDICALSUPPLIESDELIVERY, Status.PENDING, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.medicalSupply = medicalSupply;
    this.quantity = q;
    this.notes = notes;
  }

  public MedicalSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String medicalSupply,
      String q,
      String notes,
      Status status) {
    super(requestID, RequestType.MEDICALSUPPLIESDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.medicalSupply = medicalSupply;
    this.quantity = q;
    this.notes = notes;
  }
}
