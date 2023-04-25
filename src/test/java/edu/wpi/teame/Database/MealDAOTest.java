package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.MealRequestData;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class MealDAOTest {
  @Test
  public void testGetAddDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    List<MealRequestData> meal = SQLRepo.INSTANCE.getMealRequestsList();

    MealRequestData mrd =
        new MealRequestData(
            0,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Crepe au jambon",
            "tapas",
            "apple cider",
            "allergic to diyar",
            "",
            MealRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(mrd);

    List<MealRequestData> mealRequestAdded = SQLRepo.INSTANCE.getMealRequestsList();
    assertEquals(mealRequestAdded.size(), meal.size() + 1);

    SQLRepo.INSTANCE.deleteServiceRequest(mrd);

    List<MealRequestData> mealRequestDeleted = SQLRepo.INSTANCE.getMealRequestsList();
    assertEquals(mealRequestDeleted.size(), meal.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    MealRequestData mealRequest =
        new MealRequestData(
            0,
            "joseph",
            "Cafe",
            "2023-04-07",
            "3:12PM",
            "Joseph",
            "Crepe au jambon",
            "tapas",
            "apple cider",
            "allergic to diyar",
            "",
            MealRequestData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(mealRequest);
    SQLRepo.INSTANCE.updateServiceRequest(mealRequest, "status", "DONE");
    SQLRepo.INSTANCE.deleteServiceRequest(mealRequest);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "MealService";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.MEAL_REQUESTS, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.MEAL_REQUESTS, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
