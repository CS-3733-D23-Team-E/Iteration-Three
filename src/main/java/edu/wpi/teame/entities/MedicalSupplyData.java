package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class MedicalSupplyData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String notes;
  @Getter @Setter private String bandaid;
  @Getter @Setter private String covidtest;
  @Getter @Setter private String epipen;
  @Getter @Setter private String gloves;
  @Getter @Setter private String sthetiscope;
  @Getter @Setter private String syringe;

  public MedicalSupplyData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String notes,
      String bandaid,
      String covidtest,
      String epipen,
      String gloves,
      String sthetiscope,
      String syringe) {
    super(requestID, RequestType.MEDICALSUPPLY, Status.PENDING, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.notes = notes;
    this.bandaid = bandaid;
    this.covidtest = covidtest;
    this.epipen = epipen;
    this.gloves = gloves;
    this.sthetiscope = sthetiscope;
    this.syringe = syringe;
  }

  public MedicalSupplyData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String notes,
      String bandaid,
      String covidtest,
      String epipen,
      String gloves,
      String sthetiscope,
      String syringe,
      Status status) {
    super(requestID, RequestType.MEDICALSUPPLY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.notes = notes;
    this.bandaid = bandaid;
    this.covidtest = covidtest;
    this.epipen = epipen;
    this.gloves = gloves;
    this.sthetiscope = sthetiscope;
    this.syringe = syringe;
  }
}
