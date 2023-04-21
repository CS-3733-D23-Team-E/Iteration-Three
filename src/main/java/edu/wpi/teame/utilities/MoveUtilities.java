package edu.wpi.teame.utilities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.MoveAttribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class MoveUtilities {
  Date today;
  SimpleDateFormat formatter;

  public MoveUtilities() {
    today = new Date();
    formatter = new SimpleDateFormat("yyyy-MM-dd");
  }

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
            .filter(movAt -> afterDate(movAt, date) >= 0)
            .toList();

    return null;
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
}
