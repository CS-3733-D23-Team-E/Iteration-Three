package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.AlertData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class AlertDAOTest {

  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<AlertData> alerts = SQLRepo.INSTANCE.getAlertList();
    int originalSize = alerts.size();

    AlertData ad = new AlertData(0, "This is a test");
    SQLRepo.INSTANCE.addAlert(ad);
    List<AlertData> addedSize = SQLRepo.INSTANCE.getAlertList();
    int addSize = addedSize.size();
    assertEquals(originalSize + 1, addSize);

    SQLRepo.INSTANCE.deleteAlert(ad);
    List<AlertData> deletedSize = SQLRepo.INSTANCE.getAlertList();
    int deleted = deletedSize.size();
    assertEquals(originalSize, deleted);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    AlertData ad = new AlertData(0, "This is a test");

    SQLRepo.INSTANCE.addAlert(ad);
    SQLRepo.INSTANCE.updateAlert(ad, "message", "Hopefully this test works");

    SQLRepo.INSTANCE.deleteAlert(ad);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.ALERT, desktopPath, "Alert");
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.ALERT, desktopPath + "\\Alert");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
