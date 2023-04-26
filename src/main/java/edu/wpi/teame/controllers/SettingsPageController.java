package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.Settings;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class SettingsPageController {

  @FXML MFXButton englishButton;
  @FXML MFXButton spanishButton;
  @FXML MFXButton frenchButton;

  @FXML MFXButton hawaiianButton;

  @FXML
  public void initialize() {

    englishButton.setOnMouseClicked(
        event -> Settings.INSTANCE.setLanguage(Settings.Language.ENGLISH));
    spanishButton.setOnMouseClicked(
        event -> Settings.INSTANCE.setLanguage(Settings.Language.SPANISH));
    hawaiianButton.setOnMouseClicked(
        event -> Settings.INSTANCE.setLanguage(Settings.Language.HAWAIIAN));
    frenchButton.setOnMouseClicked(
        event -> Settings.INSTANCE.setLanguage(Settings.Language.FRENCH));
  }
}
