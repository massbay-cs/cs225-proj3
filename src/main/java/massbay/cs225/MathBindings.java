package massbay.cs225;

import com.sun.javafx.collections.ImmutableObservableList;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MathBindings {
    private MathBindings() {
        throw new UnsupportedOperationException();
    }

    public static DoubleBinding sin(ObservableDoubleValue n) {
        return unary(Math::sin, n);
    }

    public static DoubleBinding cos(ObservableDoubleValue n) {
        return unary(Math::cos, n);
    }

    public static DoubleBinding sqrt(ObservableDoubleValue n) {
        return unary(Math::sqrt, n);
    }

    public static DoubleBinding abs(ObservableDoubleValue n) {
        return unary(Math::abs, n);
    }

    public static DoubleBinding floor(ObservableDoubleValue n) {
        return unary(Math::floor, n);
    }

    public static DoubleBinding ceil(ObservableDoubleValue n) {
        return unary(Math::ceil, n);
    }

    public static DoubleBinding rot(ObservableDoubleValue n) {
        return unary(x -> x - Math.floor(x), n);
    }

    public static DoubleBinding unary(Function<Double, Double> op, ObservableDoubleValue n) {
        return new UnaryDoubleBinding(op, n);
    }

    public static DoubleBinding binary(BiFunction<Double, Double, Double> op, ObservableDoubleValue a, ObservableDoubleValue b) {
        return new BinaryDoubleBinding(op, a, b);
    }

    private final static class UnaryDoubleBinding extends DoubleBinding {
        private final Function<Double, Double> op;
        private final ObservableDoubleValue n;

        public UnaryDoubleBinding(Function<Double, Double> op, ObservableDoubleValue n) {
            this.op = op;
            this.n = n;

            bind(n);
        }

        @Override
        protected double computeValue() {
            return op.apply(n.get());
        }

        @Override
        public void dispose() {
            unbind(n);

            super.dispose();
        }

        @Override
        public ObservableList<?> getDependencies() {
            return FXCollections.singletonObservableList(n);
        }
    }

    private final static class BinaryDoubleBinding extends DoubleBinding {
        private final BiFunction<Double, Double, Double> op;
        private final ObservableDoubleValue a, b;

        public BinaryDoubleBinding(BiFunction<Double, Double, Double> op, ObservableDoubleValue a, ObservableDoubleValue b) {
            this.op = op;
            this.a = a;
            this.b = b;

            bind(a, b);
        }

        @Override
        protected double computeValue() {
            return op.apply(a.get(), b.get());
        }

        @Override
        public void dispose() {
            unbind(a, b);

            super.dispose();
        }

        @Override
        public ObservableList<?> getDependencies() {
            return new ImmutableObservableList<>(a, b);
        }
    }
}
