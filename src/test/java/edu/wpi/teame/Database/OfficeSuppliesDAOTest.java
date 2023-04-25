package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.OfficeSuppliesData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class OfficeSuppliesDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<OfficeSuppliesData> officeSupply = SQLRepo.INSTANCE.getOfficeSupplyList();

    OfficeSuppliesData ofd =
        new OfficeSuppliesData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "rulers",
            "2",
            "fast",
            OfficeSuppliesData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(ofd);

    List<OfficeSuppliesData> officeSupplyRequestAdded = SQLRepo.INSTANCE.getOfficeSupplyList();
    assertEquals(officeSupplyRequestAdded.size(), officeSupply.size() + 1);

    SQLRepo.INSTANCE.deleteServiceRequest(ofd);
    List<OfficeSuppliesData> officeSupplyRequestDeleted = SQLRepo.INSTANCE.getOfficeSupplyList();
    assertEquals(officeSupplyRequestDeleted.size(), officeSupply.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    OfficeSuppliesData officeSupplyRequest =
        new OfficeSuppliesData(
            0,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "rulers",
            "2",
            "fast",
            OfficeSuppliesData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(officeSupplyRequest);
    SQLRepo.INSTANCE.updateServiceRequest(officeSupplyRequest, "status", "DONE");
    SQLRepo.INSTANCE.deleteServiceRequest(officeSupplyRequest);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "OfficeSupplies";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.OFFICE_SUPPLY, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.OFFICE_SUPPLY, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
