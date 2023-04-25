package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class MealRequestData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String mainCourse;
  @Getter @Setter private String sideCourse;
  @Getter @Setter private String drink;

  @Getter @Setter private String allergies;

  @Getter @Setter private String notes;

  public MealRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String mainCourse,
      String sideCourse,
      String drink,
      String allergies,
      String notes) {
    super(requestID, RequestType.MEALDELIVERY, Status.PENDING, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.mainCourse = mainCourse;
    this.sideCourse = sideCourse;
    this.drink = drink;
    this.allergies = allergies;
    this.notes = notes;
  }

  public MealRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String mainCourse,
      String sideCourse,
      String drink,
      String allergies,
      String notes,
      Status status) {
    super(requestID, RequestType.MEALDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.mainCourse = mainCourse;
    this.sideCourse = sideCourse;
    this.drink = drink;
    this.allergies = allergies;
    this.notes = notes;
  }
}
