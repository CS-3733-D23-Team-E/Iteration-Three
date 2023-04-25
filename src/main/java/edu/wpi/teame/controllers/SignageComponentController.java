package edu.wpi.teame.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import org.controlsfx.control.SearchableComboBox;

public class SignageComponentController {
    @FXML MFXButton submitButton;
    @FXML MFXButton cancelButton;
    @FXML MFXButton resetButton;
    @FXML DatePicker deliveryDateHere;
    @FXML DatePicker deliveryDateUp;
    @FXML DatePicker deliveryDateDown;
    @FXML DatePicker deliveryDateLeft;
    @FXML DatePicker deliveryDateRight;
    @FXML SearchableComboBox<String> kioskLocationHere;
    @FXML SearchableComboBox<String> kioskLocationUp;
    @FXML SearchableComboBox<String> kioskLocationDown;
    @FXML SearchableComboBox<String> kioskLocationLeft;
    @FXML SearchableComboBox<String> kioskLocationRight;
}
