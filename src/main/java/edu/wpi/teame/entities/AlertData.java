package edu.wpi.teame.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

public class AlertData {
  @Getter @Setter int alertID;
  @Getter @Setter private String message;
  @Getter @Setter private String timestamp;
  @Getter LocalDateTime time;

  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

  public AlertData(int alertID, String message) {
    this.alertID = alertID;
    this.message = message;

    time = LocalDateTime.now();

    this.timestamp = dtf.format(time);
  }

  public AlertData(int alertID, String message, String timestamp) {
    this.alertID = alertID;
    this.message = message;
    this.timestamp = timestamp;

    time = LocalDateTime.parse(timestamp, dtf);
  }
}
