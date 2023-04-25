package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.Settings;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class SettingsPageController {

  @FXML MFXButton englishButton;

  @FXML MFXButton hawaiianButton;

  @FXML
  public void initialize() {
    Settings.INSTANCE.setLanguage(Settings.Language.ENGLISH);
  }
}
