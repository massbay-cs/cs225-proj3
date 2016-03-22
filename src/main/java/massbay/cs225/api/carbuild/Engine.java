package massbay.cs225.api.carbuild;

import lombok.*;
import massbay.cs225.api.carbuild.annotation.CarField;
import massbay.cs225.api.carbuild.annotation.CarFieldType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Engine extends MutableCarComponent {
    @CarField(label = "api.field.horsepower", type = CarFieldType.NONNEGATIVE_DOUBLE)
    private double horsepower;

    @CarField(label = "api.field.cylinders", type = CarFieldType.NONNEGATIVE_INT)
    private int cylinders;

    @CarField(label = "api.field.efficiency", type = CarFieldType.PERCENTAGE)
    private double efficiency;
}