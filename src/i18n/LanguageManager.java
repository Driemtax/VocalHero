package i18n;

import java.util.*;
import utils.LanguagePreferences;

public class LanguageManager {
    private static Locale currentLocale = LanguagePreferences.loadLanguage();
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n.Messages", currentLocale);

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("i18n.Messages", currentLocale);
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
}