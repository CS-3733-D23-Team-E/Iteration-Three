package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.NodeInitializer;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class NodeDAOTest {

  @Test
  public void testResetDatabase() {
    SQLRepo.INSTANCE.connectToDatabase("admin", "admin");
    SQLRepo.INSTANCE.resetDatabase();
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testGetAddandDelete() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<HospitalNode> originalNodes = SQLRepo.INSTANCE.getNodeList();
    assertEquals(581, originalNodes.size());

    SQLRepo.INSTANCE.addNode(new HospitalNode(new NodeInitializer("0", 0, 0, "L2", "Test")));
    List<HospitalNode> addedNodes = SQLRepo.INSTANCE.getNodeList();
    assertEquals(582, addedNodes.size());

    SQLRepo.INSTANCE.deletenode(new HospitalNode(new NodeInitializer("0", 0, 0, "L2", "Test")));
    List<HospitalNode> deletedNodes = SQLRepo.INSTANCE.getNodeList();
    assertEquals(581, deletedNodes.size());

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdate() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    HospitalNode hn = new HospitalNode(new NodeInitializer("1200", 1608, 2737, "1", "BTM"));
    SQLRepo.INSTANCE.updateNode(hn, "floor", "1");

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "Node";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.NODE, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.NODE, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
