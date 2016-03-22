package massbay.cs225.api.carbuild;

import massbay.cs225.api.Nameable;

public interface CarComponent extends Nameable {
    boolean isMutable();
}
