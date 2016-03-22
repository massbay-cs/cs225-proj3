package massbay.cs225.api.carbuild;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import massbay.cs225.api.Nameable;
import static massbay.cs225.util.I18n.*;

public enum ComponentType implements Nameable {
    ENGINE(s("api.engine"), Engine.class),
    WHEELS(s("api.wheels"), Wheels.class),
    TIRES(s("api.tires"), Tires.class),
    GAS_TANK(s("api.gasTank"), GasTank.class),
    GAS_TYPE(s("api.gasType"), GasType.class),
    ;

    private final StringProperty name;
    @Getter
    private final Class<? extends CarComponent> type;
    @Getter
    private final boolean typeMutable;

    ComponentType(String name, Class<? extends CarComponent> type) {
        this.name = new ReadOnlyStringWrapper(this, "name", name);
        this.type = type;
        this.typeMutable = MutableCarComponent.class.isAssignableFrom(type);
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
