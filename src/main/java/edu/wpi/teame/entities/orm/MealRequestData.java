package edu.wpi.teame.entities.orm;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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

  public String getTable() {
    return "MealDelivery";
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
    if (changes.containsKey("mainCourse")) {
      this.mainCourse = changes.get("mainCourse");
    }
    if (changes.containsKey("sideCourse")) {
      this.sideCourse = changes.get("sideCourse");
    }
    if (changes.containsKey("drink")) {
      this.drink = changes.get("drink");
    }
    if (changes.containsKey("allergies")) {
      this.allergies = changes.get("allergies");
    }
    if (changes.containsKey("notes")) {
      this.notes = changes.get("notes");
    }
  }
}
