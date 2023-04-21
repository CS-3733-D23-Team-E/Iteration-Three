package edu.wpi.teame.Database;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teame.entities.orm.MoveAttribute;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.junit.jupiter.api.Test;

public class MoveDAOTest {
  @Test
  public void getMove() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<MoveAttribute> moveAttributeList = SQLRepo.INSTANCE.getMoveList();
    assertFalse(moveAttributeList.isEmpty());
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testUpdateList() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    SQLRepo.INSTANCE.getMoveList();

    // add update
    SQLRepo.INSTANCE.updateMove(
        new MoveAttribute(1200, "Hall 3 Level 1", "2023-01-01"), "date", "2023-01-02");

    // reset update
    SQLRepo.INSTANCE.updateMove(
        new MoveAttribute(1200, "Hall 3 Level 1", "2023-01-02"), "date", "2023-01-01");
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testAddAndDeleteMove() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");
    List<MoveAttribute> moveAttributes = SQLRepo.INSTANCE.getMoveList();

    SQLRepo.INSTANCE.addMove(new MoveAttribute(2535, "HallNode", "2023-01-01"));
    List<MoveAttribute> moveAdded = SQLRepo.INSTANCE.getMoveList();

    assertEquals(moveAttributes.size() + 1, moveAdded.size());

    SQLRepo.INSTANCE.deleteMove(new MoveAttribute(2535, "HallNode", "2023-01-01"));

    List<MoveAttribute> deletedMove = SQLRepo.INSTANCE.getMoveList();

    assertEquals(moveAttributes.size(), deletedMove.size());
    SQLRepo.INSTANCE.exitDatabaseProgram();
  }

  @Test
  public void testImportExport() {
    SQLRepo.INSTANCE.connectToDatabase("teame", "teame50");

    FileSystemView view = FileSystemView.getFileSystemView();
    File file = view.getHomeDirectory();
    String desktopPath = file.getPath();

    String tableName = "Move";

    SQLRepo.INSTANCE.exportToCSV(SQLRepo.Table.MOVE, desktopPath, tableName);
    SQLRepo.INSTANCE.importFromCSV(SQLRepo.Table.MOVE, desktopPath + "\\" + tableName);

    SQLRepo.INSTANCE.exitDatabaseProgram();
  }
}
