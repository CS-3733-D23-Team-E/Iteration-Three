package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.map.LocationName;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class LocationNameDAOTest {
  @Test
  public void getLocationName() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<LocationName> locationNameList = SQLRepo.INSTANCE.getLocationList();
    assertFalse(locationNameList.isEmpty());
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdateList() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    SQLRepo.INSTANCE.getLocationList();

    // update
    SQLRepo.INSTANCE.updateLocation(
        new LocationName("Hall 1 Level 2", "Hall", LocationName.NodeType.HALL),
        "shortName",
        "Test");

    // reset update
    SQLRepo.INSTANCE.updateLocation(
        new LocationName("Hall 1 Level 2", "Test", LocationName.NodeType.HALL),
        "shortName",
        "Hall");
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testAddAndDeleteLocationNameList() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<LocationName> locationNames = SQLRepo.INSTANCE.getLocationList();

    int lengthList = locationNames.size();

    SQLRepo.INSTANCE.addLocation(new LocationName("Cafe 1", "Test", LocationName.NodeType.REST));

    locationNames = SQLRepo.INSTANCE.getLocationList();

    assertTrue(locationNames.size() == lengthList + 1);

    SQLRepo.INSTANCE.deleteLocation(new LocationName("Cafe 1", "Test", LocationName.NodeType.REST));

    locationNames = SQLRepo.INSTANCE.getLocationList();

    assertTrue(locationNames.size() == lengthList);
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "LocationName";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.LOCATION_NAME, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.LOCATION_NAME, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
