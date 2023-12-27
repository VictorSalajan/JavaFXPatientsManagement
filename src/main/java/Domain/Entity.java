package Domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    protected int ID;

    Entity(int ID) {
        this.ID = ID;
    }

    public abstract int getID();

    @Override
    public boolean equals(Object o) {               // necessary when using entities.indexOf(newEntity) in Repo.update, newEntity having a diff obj ref than what was at that index, even if the objects are identical in attr content
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
//      Compare the IDs of the entities
        Entity entity = (Entity) o;
        return this.getID() == entity.getID();
    }
}