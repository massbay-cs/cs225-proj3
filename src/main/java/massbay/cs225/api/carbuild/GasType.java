package massbay.cs225.api.carbuild;

import javafx.beans.property.*;
import lombok.Getter;
import massbay.cs225.api.carbuild.annotation.CarField;
import massbay.cs225.api.carbuild.annotation.CarFieldType;

public enum GasType implements CarComponent {
    GASOLINE("Gasoline", 1.0),  // TODO
    DIESEL("Diesel", 1.0);    // TODO

    private final StringProperty name;

    @CarField(label = "api.field.efficiency", type = CarFieldType.PERCENTAGE, readonly = true)
    @Getter
    private final double efficiency;

    GasType(String name, double efficiency) {
        this.name = new ReadOnlyStringWrapper(this, "name", name);
        this.efficiency = efficiency;
    }

    @Override
    public boolean isRenameable() {
        return false;
    }

    @Override
    public void setName(String name) {
        throw new UnsupportedOperationException(getClass().getName() + " is not renameable.");
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }
}