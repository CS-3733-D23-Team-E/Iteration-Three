package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.map.HospitalEdge;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class EdgeDAOTest {

  @Test
  public void testGetEdgeList() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<HospitalEdge> edgeList = SQLRepo.INSTANCE.getEdgeList();

    assertFalse(edgeList.isEmpty());
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdateList() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    SQLRepo.INSTANCE.getEdgeList();

    // update
    SQLRepo.INSTANCE.updateEdge(new HospitalEdge("2315", "1875"), "endNode", "1140");

    // reset update
    SQLRepo.INSTANCE.updateEdge(new HospitalEdge("2315", "1140"), "endNode", "1875");
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testAddAndDeleteEdgeList() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<HospitalEdge> edgeList = SQLRepo.INSTANCE.getEdgeList();

    int lengthList = edgeList.size();

    SQLRepo.INSTANCE.addEdge(new HospitalEdge("2315", "1140"));

    edgeList = SQLRepo.INSTANCE.getEdgeList();

    assertTrue(edgeList.size() == lengthList + 1);

    SQLRepo.INSTANCE.deleteEdge(new HospitalEdge("2315", "1140"));

    edgeList = SQLRepo.INSTANCE.getEdgeList();

    assertTrue(edgeList.size() == lengthList);
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void exportEdge() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.EDGE, desktopPath, "Edge");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void importEdge() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.EDGE, desktopPath + "\\Edge");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
