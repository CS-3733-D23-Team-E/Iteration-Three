package edu.wpi.teame.entities;

import edu.wpi.teame.entities.orm.ORM;
import lombok.Getter;

import java.util.HashMap;

public class Diff<E extends ORM>{

    @Getter private final E old;
    @Getter private final HashMap<String, String> changes = new HashMap<>();

    public Diff(E old){
        this.old = old;
    }

    public void addChange(String field, String newValue){
        changes.put(field, newValue);
    }

    public void applyChanges(){
        old.applyChanges(changes);
    }
}
