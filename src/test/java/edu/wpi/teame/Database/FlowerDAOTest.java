package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.orm.FlowerRequestData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class FlowerDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<FlowerRequestData> flower = SQLRepo.INSTANCE.getFlowerRequestsList();

    FlowerRequestData frd =
        new FlowerRequestData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "rose",
            "2",
            "yes",
            "i love you babe",
            "no package",
            FlowerRequestData.Status.IN_PROGRESS);
    SQLRepo.INSTANCE.addServiceRequest(frd);

    List<FlowerRequestData> flowerRequestAdded = SQLRepo.INSTANCE.getFlowerRequestsList();
    assertEquals(flower.size() + 1, flowerRequestAdded.size());

    SQLRepo.INSTANCE.deleteServiceRequest(frd);

    List<FlowerRequestData> flowerRequestDeleted = SQLRepo.INSTANCE.getFlowerRequestsList();
    assertEquals(flower.size(), flowerRequestDeleted.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FlowerRequestData flowerRequest =
        new FlowerRequestData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "rose",
            "2",
            "yes",
            "i love you babe",
            "no package",
            FlowerRequestData.Status.IN_PROGRESS);
    SQLRepo.INSTANCE.addServiceRequest(flowerRequest);
    SQLRepo.INSTANCE.updateServiceRequest(flowerRequest, "status", "DONE");
    SQLRepo.INSTANCE.deleteServiceRequest(flowerRequest);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.FLOWER_REQUESTS, desktopPath, "FlowerService");
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.FLOWER_REQUESTS, desktopPath + "\\FlowerService");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
