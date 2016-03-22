package massbay.cs225.api.carbuild;

import lombok.*;
import massbay.cs225.api.carbuild.annotation.CarField;
import massbay.cs225.api.carbuild.annotation.CarFieldType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Wheels extends MutableCarComponent {
    @CarField(label = "api.field.badWeatherHandling", type = CarFieldType.PERCENTAGE)
    private double badWeatherHandling;  // percentage (0 to 1)

    @CarField(label = "api.field.turnHandling", type = CarFieldType.PERCENTAGE)
    private double turnHandling;  // percentage (0 to 1); lower is better on straight roads
}