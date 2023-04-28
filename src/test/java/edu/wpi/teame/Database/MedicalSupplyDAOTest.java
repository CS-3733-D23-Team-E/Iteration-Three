package edu.wpi.teame.Database;

import edu.wpi.teame.entities.MedicalSuppliesData;
import edu.wpi.teame.entities.MedicalSupplyData;
import edu.wpi.teame.entities.ServiceRequestData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class MedicalSupplyDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<MedicalSupplyData> medicalSuppliesData = SQLRepo.INSTANCE.getMedicalSupply();

    MedicalSupplyData ofd =
        new MedicalSupplyData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Needle",
            "2",
            "3",
            "0",
            "0",
            "0",
            "0",
            ServiceRequestData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(ofd);
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<MedicalSuppliesData> medicalSuppliesData = SQLRepo.INSTANCE.getMedicalSuppliesList();

    MedicalSuppliesData ofd =
        new MedicalSuppliesData(
            1,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Needle",
            "2",
            "fast",
            ServiceRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(ofd);

    SQLRepo.INSTANCE.updateServiceRequest(ofd, "status", "DONE");
    // SQLRepo.INSTANCE.deleteServiceRequest(ofd);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "MedicalSupplies";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.MEDICAL_SUPPLIES, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.MEDICAL_SUPPLIES, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
