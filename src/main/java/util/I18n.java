package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("GameBundle", Locale.getDefault());

    private I18n() {
        throw new UnsupportedOperationException();
    }

    public static String s(String key) {
        return bundle.getString(key);
    }

    public static String s(String key, Object... args) {
        return String.format(bundle.getString(key), args);
    }
}
