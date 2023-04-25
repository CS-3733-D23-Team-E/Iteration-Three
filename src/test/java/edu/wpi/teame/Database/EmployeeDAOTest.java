package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.Employee;
import java.io.File;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class EmployeeDAOTest {

  @Test
  public void testlogIn() throws RuntimeException {
    SQLRepo.INSTANCE.connectToDatabase("staff", "staff");
    SQLRepo.INSTANCE.exitDatabaseProgram();

    SQLRepo.INSTANCE.connectToDatabase("admin", "admin");
    SQLRepo.INSTANCE.exitDatabaseProgram();

    Employee failure = SQLRepo.INSTANCE.connectToDatabase("test", "fail");
    assertNull(failure);
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testGetAddandDelete() {
    SQLRepo.INSTANCE.connectToDatabase("staff", "staff");
    int numEmployees = SQLRepo.INSTANCE.getEmployeeList().size();

    Employee Jamie = new Employee("Jamie Rapal", "JRapal", "password", "ADMIN");
    SQLRepo.INSTANCE.addEmployee(Jamie);
    int numEmployees2 = SQLRepo.INSTANCE.getEmployeeList().size();
    assertEquals(numEmployees + 1, numEmployees2);

    SQLRepo.INSTANCE.deleteEmployee(Jamie);
    int numEmployees3 = SQLRepo.INSTANCE.getEmployeeList().size();
    assertEquals(numEmployees, numEmployees3);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    Employee Jamie = new Employee("Jamie Rapal", "JRapal", "password", "ADMIN");
    SQLRepo.INSTANCE.addEmployee(Jamie);
    SQLRepo.INSTANCE.updateEmployee(Jamie, "fullName", "Jamie R");
    Jamie.setFullName("Jamie R");

    SQLRepo.INSTANCE.updateEmployee(Jamie, "username", "JamieRapal");
    Jamie.setUsername("JamieRapal");

    SQLRepo.INSTANCE.updateEmployee(Jamie, "password", "pass");
    SQLRepo.INSTANCE.updateEmployee(Jamie, "permission", "STAFF");

    SQLRepo.INSTANCE.deleteEmployee(Jamie);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.EMPLOYEE, desktopPath, "Employee");
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.EMPLOYEE, desktopPath + "\\Employee");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
