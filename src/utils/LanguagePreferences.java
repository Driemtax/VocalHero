package utils;

import java.util.Locale;
import java.util.prefs.Preferences;

public class LanguagePreferences {
    private static final String KEY = "language";
    private static final Preferences prefs = Preferences.userRoot().node("vocalhero/language");

    public static void saveLanguage(Locale locale) {
        prefs.put(KEY, locale.getLanguage());
    }

    public static Locale loadLanguage() {
        String lang = prefs.get(KEY, Locale.GERMAN.getLanguage());
        return lang.equals("en") ? Locale.ENGLISH : Locale.GERMAN;
    }

    public static int getSavedLanguageIndex() {
        return loadLanguage().equals(Locale.GERMAN) ? 0 : 1;
    }
}
