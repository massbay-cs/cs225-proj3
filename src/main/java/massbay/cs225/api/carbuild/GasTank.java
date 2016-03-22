package massbay.cs225.api.carbuild;

import lombok.*;
import massbay.cs225.api.carbuild.annotation.CarField;
import massbay.cs225.api.carbuild.annotation.CarFieldType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GasTank extends MutableCarComponent {
    @CarField(label = "api.field.gallons", type = CarFieldType.NONNEGATIVE_DOUBLE)
    private double gallons;
}