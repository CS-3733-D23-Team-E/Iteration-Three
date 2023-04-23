package edu.wpi.teame.entities.diffs;

import edu.wpi.teame.entities.orm.ORM;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public class Diff<E extends ORM> {

  @Getter final E obj;
  @Getter private final HashMap<String, String> changes = new HashMap<>();

  public Diff(E old) {
    this.obj = old;
  }

  public void addChange(String field, String newValue) {
    changes.put(field, newValue);
  }

  public void applyChanges() {
    obj.applyChanges(changes);
  }

  public String getQuery() {
    StringBuilder query = new StringBuilder();
    for (Map.Entry<String, String> changes : changes.entrySet()) {
      query.append("UPDATE \"" + obj.getTable() + "\" " + "SET \"" + changes.getKey() + "\" = ");
      try {
        Integer.parseInt(changes.getValue());
        query.append(changes.getValue());
      } catch (NumberFormatException e) {
        query.append("'");
        query.append(changes.getValue());
        query.append("'");
      }
      query.append(" WHERE " + obj.getPrimaryKey() + ";");
    }
    return query.toString();
  }
}
