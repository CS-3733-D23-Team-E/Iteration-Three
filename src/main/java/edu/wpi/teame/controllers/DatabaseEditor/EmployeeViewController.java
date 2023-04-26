package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.map.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import org.controlsfx.control.SearchableComboBox;

public class EmployeeViewController {

  // add & delete
  @FXML MFXButton deleteButton;
  @FXML MFXButton addEmployeeButton;

  // employee tab
  @FXML TabPane employeeTabs;
  @FXML Tab employeeTab;

  // add employee fields
  @FXML HBox employeeAddZone;
  @FXML MFXTextField newFullNameField;
  @FXML MFXTextField newUsernameField;
  @FXML MFXTextField newPasswordField;
  @FXML MFXTextField newPermissionField;

  // table data for employees
  @FXML TableView<Employee> employeeTable;
  @FXML TableColumn<Employee, String> employeeFullNameCol;
  @FXML TableColumn<Employee, String> employeeUsernameCol;
  @FXML TableColumn<Employee, String> employeePasswordCol;
  @FXML TableColumn<Employee, String> employeePermissionCol;

  // edit employee fields
  @FXML VBox editEmployeeZone;
  @FXML MFXTextField editFullNameField;
  @FXML MFXTextField editUsernameField;
  @FXML MFXTextField editPasswordField;
  @FXML SearchableComboBox<String> editPermissionChoice;

  @FXML MFXButton confirmEditButton;

  TableView activeTable;
  SQLRepo.Table activeTableEnum;

  Employee currentEmployee;

  String currentStatus = "EMPLOYEE";

  @FXML
  public void initialize() {
    Popup windowPop = new Popup();
    Label popupLabel = new Label("Error: improper formatting");
    popupLabel.setStyle("-fx-background-color: red;");
    windowPop.getContent().add(popupLabel);
    windowPop.setAutoHide(true);

    Popup confirmPop = new Popup();
    Label confirmLabel = new Label("Row added successfully");
    confirmLabel.setStyle("-fx-background-color: green;");
    confirmPop.getContent().add(confirmLabel);
    confirmPop.setAutoHide(true);

    SQLRepo dC = SQLRepo.INSTANCE;

    editFullNameField.setVisible(false);
    editUsernameField.setVisible(false);
    editPasswordField.setVisible(false);
    confirmEditButton.setVisible(false);
    editPermissionChoice.setVisible(false);

    ArrayList<String> permissions = new ArrayList<>();
    permissions.add("STAFF");
    permissions.add("ADMIN");
    editPermissionChoice.setItems(FXCollections.observableArrayList(permissions));

    confirmEditButton.setOnMouseClicked(event -> updateDatabaseStatus());

    // fill table for employees
    employeeFullNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("fullName"));
    employeeUsernameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
    employeePasswordCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("password"));
    employeePermissionCol.setCellValueFactory(
        new PropertyValueFactory<Employee, String>("permission"));

    employeeTable.setItems(FXCollections.observableArrayList(dC.getEmployeeList()));
    employeeTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              if (newSelection != null) {
                displayEditEmployee(newSelection);
              }
            });
    employeeTable.setEditable(true);

    employeeTable.setPlaceholder(new Label("No Employees to Display"));

    deleteButton.setOnMouseClicked(event -> removeItem());

    addEmployeeButton.setOnMouseClicked(event -> addEmployee(windowPop, confirmPop));
  }

  private void updateDatabaseStatus() {
    if (currentStatus.equals("EMPLOYEE")) {
      if (!editFullNameField.getText().isEmpty()) {
        SQLRepo.INSTANCE.updateEmployee(currentEmployee, "fullName", editFullNameField.getText());
      }
      if (!editUsernameField.getText().isEmpty()) {
        SQLRepo.INSTANCE.updateEmployee(currentEmployee, "username", editUsernameField.getText());
      }
      if (!editPasswordField.getText().isEmpty()) {
        SQLRepo.INSTANCE.updateEmployee(currentEmployee, "password", editPasswordField.getText());
      }
      if (editPermissionChoice.getValue() != null) {
        SQLRepo.INSTANCE.updateEmployee(
            currentEmployee, "permission", editPermissionChoice.getValue());
      }
      employeeTable.setItems(FXCollections.observableArrayList(SQLRepo.INSTANCE.getEmployeeList()));
      initialize();
    }
  }

  private void displayEditEmployee(Employee newSelection) {
    editEmployeeZone.setVisible(true);
    showEditEmployeeButtons();
    currentEmployee = newSelection;
    currentStatus = "EMPLOYEE";

    // Autofill the text fields with the selected row's information
    editFullNameField.setText(newSelection.getFullName());
    editUsernameField.setText(newSelection.getUsername());
    editPasswordField.setText(newSelection.getPassword());
    editPermissionChoice.setValue(newSelection.getPermission());
  }

  private void showEditEmployeeButtons() {
    editFullNameField.setVisible(true);
    editUsernameField.setVisible(true);
    editPasswordField.setVisible(true);
    editPermissionChoice.setVisible(true);
    confirmEditButton.setVisible(true);
  }

  private void addEmployee(Popup windownPop, Popup confirmPop) {
    Employee toAdd;
    String newFullName = newFullNameField.getText();
    String newUsername = newUsernameField.getText();
    String newPassword = newPasswordField.getText();
    String newPermission = newPermissionField.getText();
    try {
      toAdd = new Employee(newFullName, newUsername, newPassword, newPermission);
      SQLRepo.INSTANCE.addEmployee(toAdd);
      confirmPop.show(App.getPrimaryStage());
      employeeTable.getItems().add((Employee) toAdd);
      newFullNameField.clear();
      newUsernameField.clear();
      newPasswordField.clear();
      newPermissionField.clear();
    } catch (RuntimeException e) {
      System.out.println("error caught");
      windownPop.show(App.getPrimaryStage());
      System.out.println(e);
    }
  }

  private void removeItem() {
    Object selectedItem = employeeTable.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      employeeTable.getItems().remove(selectedItem);
      SQLRepo.INSTANCE.deleteEmployee((Employee) selectedItem);
    }
  }
}
