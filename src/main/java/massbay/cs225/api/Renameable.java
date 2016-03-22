package massbay.cs225.api;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Renameable implements Nameable {
    private final StringProperty name = new SimpleStringProperty(this, "name");

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public final boolean isRenameable() {
        return true;
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
