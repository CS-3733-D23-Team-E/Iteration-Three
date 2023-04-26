package edu.wpi.teame.utilities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.map.MoveAttribute;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class MoveUtilities {
  Date today;
  SimpleDateFormat formatter;

  HashMap<String, LocalDate> dateHashMap;

  public MoveUtilities() {
    today = new Date();
    formatter = new SimpleDateFormat("yyyy-MM-dd");
  }

  /////////////// Getters (from database) ////////////////

  /**
   * find the most recent move attribute associated with the given longName on a given day
   *
   * @param longName
   * @param date
   * @return
   */
  public MoveAttribute findMostRecentMoveByDate(String longName, Date date) {
    List<MoveAttribute> movesAtDate =
        SQLRepo.INSTANCE.getMoveList().stream()
            .filter(movAt -> (movAt.getLongName().equals(longName)))
            .filter(movAt -> afterDate(movAt, date) <= 0) // before or on date
            .sorted(
                new Comparator<MoveAttribute>() {
                  @Override
                  public int compare(MoveAttribute o1, MoveAttribute o2) {
                    try {
                      return formatter.parse(o1.getDate()).compareTo(formatter.parse(o2.getDate()));
                    } catch (ParseException e) {
                      System.out.println(e);
                      return 0;
                    }
                  }
                })
            .toList();

    try {
      return movesAtDate.get(movesAtDate.size() - 1);
    } catch (IndexOutOfBoundsException e) {
      System.out.println("This location does not have a node for this date");
      return null;
    }
  }

  /**
   * Finds the most recent move attribute associated with the given longName today
   *
   * @param longName
   * @return
   */
  public MoveAttribute findMostRecentMoveByDate(String longName) {
    return findMostRecentMoveByDate(longName, today);
  }

  /**
   * converts a LocalDate into a Date (for method purposes)
   *
   * @param localDate
   * @return
   */
  public Date toDateFromLocal(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  /**
   * determines when a move is relative to a LocalDate
   *
   * @param move
   * @param localDate
   * @return 0 if the dates occur on the same day, -1 if the move is before, 1 is it is after
   */
  public int afterDate(MoveAttribute move, LocalDate localDate) {
    // cast localDate to Date
    return afterDate(move, toDateFromLocal(localDate));
  }

  /**
   * determines when a move is relative to today
   *
   * @param move
   * @return 0 if the dates occur on the same day, -1 if the move is before, 1 is it is after
   */
  public int afterDate(MoveAttribute move) {
    return afterDate(move, today);
  }

  /**
   * determines when a move is relative to a Date
   *
   * @param move
   * @param day
   * @return 0 if the dates occur on the same day, -1 if the move is before, 1 is it is after
   */
  public int afterDate(MoveAttribute move, Date day) {
    Date moveDate;
    try {
      moveDate = formatter.parse(move.getDate());
    } catch (ParseException e) {
      moveDate = new Date();
      System.out.println(e);
    }

    if (formatter.format(day).equals(formatter.format(moveDate))) return 0;

    return moveDate
        .toInstant()
        .truncatedTo(ChronoUnit.DAYS)
        .compareTo(day.toInstant().truncatedTo(ChronoUnit.DAYS));
  }

  /**
   * gets all the moves that happen today
   *
   * @return
   */
  public List<MoveAttribute> getCurrentMoves() {
    return SQLRepo.INSTANCE.getMoveList().stream().filter(move -> afterDate(move) == 0).toList();
  }

  /**
   * gets all the moves that are in the future
   *
   * @return
   */
  public List<MoveAttribute> getFutureMoves() {
    return SQLRepo.INSTANCE.getMoveList().stream().filter(move -> afterDate(move) > 0).toList();
  }

  /**
   * gets the move message associated all the moves that happen today
   *
   * @return
   */
  public List<String> getCurrentMoveMessages() {
    return getCurrentMoves().stream()
        .map(move -> move.getLongName() + " to Node " + move.getNodeID())
        .toList();
  }

  /**
   * gets a list of only the moves at happen for departments
   *
   * @return
   */
  public List<MoveAttribute> getMovesForDepartments() {
    return SQLRepo.INSTANCE.getMoveList().stream()
        .filter(
            (move) -> // Filter out hallways and long names with no corresponding
                // LocationName
                LocationName.allLocations.get(move.getLongName()) != null
                    && LocationName.allLocations.get(move.getLongName()).getNodeType()
                        == LocationName.NodeType
                            .DEPT) // NOTE: Before this statement was just filtering out Hall,
        // Stair, Elevator, and Restrooms
        .toList();
  }

  /**
   * formats today into the formatter's form
   *
   * @return
   */
  public String formatToday() {
    return formatDate(today);
  }

  /**
   * formats date into the formatter's form (YYYY-MM-DD)
   *
   * @return
   */
  public String formatDate(Date date) {
    return formatter.format(date);
  }

  public HashMap<String, String> getMapForDate(LocalDate date) {
    HashMap<String, String> map = new HashMap<>();
    dateHashMap = new HashMap<>();
    for (MoveAttribute move : SQLRepo.INSTANCE.getMoveList()) {
      if (date.compareTo(LocalDate.parse(move.getDate())) < 0)
        // Move hasn't happened yet
        continue;
      if (dateHashMap.containsKey(move.getLongName())) {
        if (dateHashMap.get(move.getLongName()).compareTo(LocalDate.parse(move.getDate())) < 0) {
          // Move is more recent than one already in the map, remove the old one
          dateHashMap.remove(move.getLongName());
          map.remove(move.getLongName());
        }
      }
      dateHashMap.put(move.getLongName(), LocalDate.parse(move.getDate()));
      map.put(move.getLongName(), move.getNodeID() + "");
    }
    return map;
  }

  public HashMap<String, String> invertHashMap(HashMap<String, String> map) {
    HashMap<String, String> invertedMap = new HashMap<>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      if (invertedMap.containsKey(entry.getValue())) {
        if (dateHashMap
                .get(invertedMap.get(entry.getValue()))
                .compareTo(dateHashMap.get(entry.getKey()))
            < 0) {
          // Move is more recent than one already in the map, remove the old one
          dateHashMap.remove(entry.getValue());
          invertedMap.remove(entry.getValue());
        } else
          continue; // Move is less recent than one already in the map, ignore it (don't add it)
      }
      invertedMap.put(entry.getValue(), entry.getKey());
    }
    return invertedMap;
  }

  public int daysCompareMove(String longName, LocalDate date) {
    int days = Integer.MAX_VALUE;
    List<MoveAttribute> movesForNode =
        SQLRepo.INSTANCE.getMoveList().stream()
            .filter(move -> (move.getLongName()).equals(longName))
            .toList();
    for (MoveAttribute move : movesForNode) {
      if (Math.abs(date.until(LocalDate.parse(move.getDate())).getDays()) < Math.abs(days))
        days = date.until(LocalDate.parse(move.getDate())).getDays();
    }
    return days;
  }

  public static void main(String[] args) {
    MoveUtilities moveUtilities = new MoveUtilities();
    System.out.println();
    System.out.println(moveUtilities.daysCompareMove("CART Waiting", LocalDate.now()));
  }

  ////////////////// Setters (sending new move data to database) ///////////////////////
}
