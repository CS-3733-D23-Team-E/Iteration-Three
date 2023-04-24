package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.SignageComponentData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class SignageComponentDAOTest {

  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<SignageComponentData> signage = SQLRepo.INSTANCE.getSignageList();

    SignageComponentData crd =
        new SignageComponentData(
            "2023-04-22",
            "Screen 2, By the Q Elevator",
            "Watkins Clinics A & B",
            SignageComponentData.arrowDirections.UP);

    SQLRepo.INSTANCE.addSignage(crd);
    List<SignageComponentData> signageAdded = SQLRepo.INSTANCE.getSignageList();

    assertEquals(signage.size() + 1, signageAdded.size());

    SQLRepo.INSTANCE.deleteSignage(crd);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<SignageComponentData> signage = SQLRepo.INSTANCE.getSignageList();

    SignageComponentData crd =
        new SignageComponentData(
            "2023-04-22",
            "Screen 1, By the info desk",
            "Shapiro Admitting",
            SignageComponentData.arrowDirections.LEFT);

    // SQLRepo.INSTANCE.addSignage(crd);
    SQLRepo.INSTANCE.updateSignage(crd, "arrowDirection", "RIGHT");
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.SIGNAGE_FORM, desktopPath, "SignageForm");

    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.CONFERENCE_ROOM, desktopPath + "\\SignageForm");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
