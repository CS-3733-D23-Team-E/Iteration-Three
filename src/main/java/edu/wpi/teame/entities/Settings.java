package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public enum Settings {
  INSTANCE;

  public String nyay = "\u00F1"; // ñ
  public String aA = "\u0301"; // á
  public String aE = "\u00E9"; // é
  public String aI = "\u00ED"; // í
  public String aO = "\u00F3"; // ó
  public String aU = "\u00F9"; // ù

  public enum Language {
    ENGLISH,
    SPANISH,
    FRENCH,
    HAWAIIAN;
  }

  @Getter @Setter Language language;

  Settings() {}
}
