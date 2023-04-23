package edu.wpi.teame.entities.diffs;

import edu.wpi.teame.entities.orm.ORM;

public class RemoveDiff<E extends ORM> extends Diff<E> {
    public RemoveDiff(E toBeRemoved) {
        super(toBeRemoved);
    }

    public String getQuery(){
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM \"");
        query.append(getObj().getTable());
        query.append("\" WHERE ");
        query.append(getObj().getPrimaryKey());

        return query.toString();
    }
}
