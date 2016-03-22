package massbay.cs225.api.carbuild;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import massbay.cs225.api.Renameable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class CarBuild extends Renameable {
    private Wheels wheels;
    private Tires tires;
    private Engine engine;

    @Override
    public String toString() {
        return getName();
    }
}