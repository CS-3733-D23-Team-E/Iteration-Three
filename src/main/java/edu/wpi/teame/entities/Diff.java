package edu.wpi.teame.entities;

import edu.wpi.teame.entities.orm.ORM;
import java.util.HashMap;
import lombok.Getter;

public class Diff<E extends ORM> {

  @Getter private final E old;
  @Getter private final HashMap<String, String> changes = new HashMap<>();

  public Diff(E old) {
    this.old = old;
  }

  public void addChange(String field, String newValue) {
    changes.put(field, newValue);
  }

  public void applyChanges() {
    old.applyChanges(changes);
  }
}
