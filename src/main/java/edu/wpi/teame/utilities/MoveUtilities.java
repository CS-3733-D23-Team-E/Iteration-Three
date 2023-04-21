package edu.wpi.teame.utilities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.map.MoveAttribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MoveUtilities {
  Date today;
  SimpleDateFormat formatter;

  public MoveUtilities() {
    today = new Date();
    formatter = new SimpleDateFormat("yyyy-MM-dd");
  }


  /////////////// Getters (from database) ////////////////

  public MoveAttribute findMoveAttribute(String longName) {
    List<MoveAttribute> listOfMoveAtt = SQLRepo.INSTANCE.getMoveList();
    for (MoveAttribute movAt : listOfMoveAtt) {
      if (longName.equals(movAt.getLongName())) {
        return movAt;
      }
    }
    return null;
  }

  public MoveAttribute findMostRecentMoveByDate(String longName, Date date) {
    List<MoveAttribute> movesAtDate =
        SQLRepo.INSTANCE.getMoveList().stream()
            .filter(movAt -> (movAt.getLongName().equals(longName)))
            .filter(movAt -> afterDate(movAt, date) <= 0) //before or on date
            .sorted(new Comparator<MoveAttribute>() {
              @Override
              public int compare(MoveAttribute o1, MoveAttribute o2){
                try {
                  return formatter.parse(o1.getDate()).compareTo(formatter.parse(o2.getDate()));
                }catch(ParseException e){
                  System.out.println(e);
                  return 0;
                }
              }
            }).toList();

    try {
      return movesAtDate.get(movesAtDate.size() - 1);
    } catch (IndexOutOfBoundsException e){
      System.out.println("This location does not have a node for this date");
      return null;
    }
  }

  public int afterDate(MoveAttribute move) {
    return afterDate(move, today);
  }

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

  public List<MoveAttribute> getCurrentMoves(){
    return SQLRepo.INSTANCE.getMoveList().stream().filter(move -> afterDate(move) == 0).toList();
  }

  public List<MoveAttribute> getFutureMoves(){
    return SQLRepo.INSTANCE.getMoveList().stream().filter(move -> afterDate(move) > 0).toList();
  }

  public List<String> getCurrentMoveMessages(){
    return getCurrentMoves().stream()
            .map(move -> move.getLongName() + " to Node " + move.getNodeID())
            .toList();
  }

  public List<MoveAttribute> getMovesForDepartments(){
    return SQLRepo.INSTANCE.getMoveList().stream()
            .filter(
                    (move) -> // Filter out hallways and long names with no corresponding
                            // LocationName
                            LocationName.allLocations.get(move.getLongName()) == null
                                    ? false
                                    : LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    == LocationName.NodeType.DEPT) //NOTE: Before this statement was just filtering out Hall, Stair, Elevator, and Restrooms
            .toList();
  }


  ////////////////// Setters (sending new move data to database) ///////////////////////
}
