package massbay.cs225.api.carbuild;

import massbay.cs225.api.Renameable;

abstract class MutableCarComponent extends Renameable implements CarComponent {
    @Override
    public boolean isMutable() {
        return true;
    }
}
