package massbay.cs225.api;

import javafx.beans.property.StringProperty;

public interface Nameable {
    String getName();
    void setName(String name);
    boolean isRenameable();
    StringProperty nameProperty();
}
