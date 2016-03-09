package massbay.cs225;

public final class Tuple<T, U> {
    private final T a;
    private final U b;

    public Tuple(T a, U b) {
        this.a = a;
        this.b = b;
    }

    public T getA() {
        return a;
    }

    public U getB() {
        return b;
    }
}
