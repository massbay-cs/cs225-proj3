package massbay.cs225;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;

public final class BoundsBindings {
    public static BooleanBinding intersect(ObservableValue<Bounds> bounds, ObservableValue<Bounds> other, String debugLeft, String debugRight) {
        return new BooleanBoundsBinding(bounds, other) {
            @Override
            protected boolean computeValue() {
                Bounds a = bounds.getValue();
                Bounds b = other.getValue();
                boolean value = a.intersects(b);
                /*if (value) {
                    System.out.printf("Intersects: %s (%.0f, %.0f):(%.0f, %.0f) with %s (%.0f, %.0f):(%.0f, %.0f)%n",
                            debugLeft,
                            a.getMinX(),
                            a.getMinY(),
                            a.getMaxX(),
                            a.getMaxY(),
                            debugRight,
                            b.getMinX(),
                            b.getMinY(),
                            b.getMaxX(),
                            b.getMaxY()
                    );
                }*/
                return value;
            }
        };
    }

    public static BooleanBinding contains(ObservableValue<Bounds> bounds, ObservableValue<Bounds> other) {
        return new BooleanBoundsBinding(bounds, other) {
            @Override
            protected boolean computeValue() {
                return bounds.getValue().contains(other.getValue());
            }
        };
    }

    public static DoubleBinding width(ObservableValue<Bounds> bounds) {
        return property(BoundsProperty.WIDTH, bounds);
    }

    public static DoubleBinding height(ObservableValue<Bounds> bounds) {
        return property(BoundsProperty.HEIGHT, bounds);
    }

    public static DoubleBinding property(BoundsProperty property, ObservableValue<Bounds> bounds) {
        return new DoubleBinding() {
            {
                bind(bounds);
            }

            @Override
            public void dispose() {
                unbind(bounds);

                super.dispose();
            }

            @Override
            protected double computeValue() {
                Bounds b = bounds.getValue();

                switch (property) {
                    case WIDTH:  return b.getWidth();
                    case HEIGHT: return b.getHeight();
                    case DEPTH:  return b.getDepth();
                    case MIN_X:  return b.getMinX();
                    case MIN_Y:  return b.getMinY();
                    case MIN_Z:  return b.getMinZ();
                    case MAX_X:  return b.getMaxX();
                    case MAX_Y:  return b.getMaxY();
                    case MAX_Z:  return b.getMaxZ();

                    default:
                        throw new IllegalStateException("Not a double property property: " + (property == null ? "(null)" : property.name()));
                }
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(bounds);
            }
        };
    }


    private static abstract class BooleanBoundsBinding extends BooleanBinding {
        private final ObservableList<ObservableValue<Bounds>> dependencies;
        private final ObservableValue<Bounds>[] dependencyArray;

        @SafeVarargs
        public BooleanBoundsBinding(ObservableValue<Bounds>... dependencies) {
            this.dependencies = new ImmutableObservableList<>(dependencies);
            this.dependencyArray = dependencies.clone();

            bind(dependencyArray);
        }

        @SuppressWarnings("unchecked")
        public BooleanBoundsBinding(ObservableValue<Bounds> dependency) {
            this.dependencies = FXCollections.singletonObservableList(dependency);
            this.dependencyArray = new ObservableValue[] { dependency };
        }

        @Override
        public void dispose() {
            unbind(dependencyArray);

            super.dispose();
        }

        @Override
        public ObservableList<?> getDependencies() {
            return dependencies;
        }
    }


    public enum BoundsProperty {
        WIDTH,
        HEIGHT,
        DEPTH,
        MIN_X,
        MIN_Y,
        MIN_Z,
        MAX_X,
        MAX_Y,
        MAX_Z,
    }
}