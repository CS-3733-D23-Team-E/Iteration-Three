package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public enum Settings {
  INSTANCE;

  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00F9"; // ù
  @Getter @Setter String language;

  Settings() {

    language = "English";
  }
}
