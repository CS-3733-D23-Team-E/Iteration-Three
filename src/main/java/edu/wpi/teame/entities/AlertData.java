package edu.wpi.teame.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

public class AlertData {
  @Getter @Setter int alertID;
  @Getter @Setter private String message;
  @Getter @Setter private String timestamp;

  public AlertData(int alertID, String message) {
    this.alertID = alertID;
    this.message = message;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    this.timestamp = dtf.format(now);
  }

  public AlertData(int alertID, String message, String timestamp) {
    this.alertID = alertID;
    this.message = message;
    this.timestamp = timestamp;
  }
}
