package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.FurnitureRequestData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class FurnitureDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<FurnitureRequestData> furniture = SQLRepo.INSTANCE.getFurnitureRequestsList();

    FurnitureRequestData frd =
        new FurnitureRequestData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Bed",
            "for 2 hours",
            FurnitureRequestData.Status.DONE);
    SQLRepo.INSTANCE.addServiceRequest(frd);

    List<FurnitureRequestData> furnitureRequestAdded = SQLRepo.INSTANCE.getFurnitureRequestsList();
    assertEquals(furniture.size() + 1, furnitureRequestAdded.size());

    SQLRepo.INSTANCE.deleteServiceRequest(frd);
    List<FurnitureRequestData> furnitureRequestDeleted =
        SQLRepo.INSTANCE.getFurnitureRequestsList();
    assertEquals(furnitureRequestDeleted.size(), furniture.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FurnitureRequestData furnitureRequest =
        new FurnitureRequestData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Bed",
            "deliver with love",
            FurnitureRequestData.Status.DONE);

    SQLRepo.INSTANCE.addServiceRequest(furnitureRequest);
    SQLRepo.INSTANCE.updateServiceRequest(furnitureRequest, "status", "PENDING");
    // SQLRepo.INSTANCE.deleteServiceRequest(furnitureRequest);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "FurnitureService";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.FURNITURE_REQUESTS, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(
        SQLRepo.Table.FURNITURE_REQUESTS, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
