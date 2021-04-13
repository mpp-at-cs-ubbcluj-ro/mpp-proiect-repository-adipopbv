package pr1Java.model;

import java.io.Serializable;

public abstract class Entity<ID> implements Serializable {

    private ID id;

    public Entity() {

    }

    public Entity(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
