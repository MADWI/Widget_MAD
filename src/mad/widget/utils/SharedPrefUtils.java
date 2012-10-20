package mad.widget.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Klasa definiujaca mozliwosci obiektu SharedPreferences
 * 
 * @author Sebastian Swierczek
 * */
public class SharedPrefUtils {

    /**
     * Metoda odpowiedzialna za pobranie danych z pliku ustawien
     * 
     * @param ctx
     *            kontekst aplikacji
     * 
     * @return pobrana dana z pliku ustawien
     */
    public static SharedPreferences getSharedPreferences(Context ctx) {
	return ctx.getSharedPreferences(Constans.PREFERENCES_NAME,
		Activity.MODE_PRIVATE);
    }

    /**
     * Metoda odpowiedzialna za wyczyszczenie pliku ustawien
     * 
     * @param ctx
     *            kontekst aplikacji
     */
    public static void clearPreferences(Context ctx) {
	SharedPreferences preferences = ctx.getSharedPreferences(
		Constans.PREFERENCES_NAME, Activity.MODE_PRIVATE);
	SharedPreferences.Editor editor = preferences.edit();
	editor.clear();
	editor.commit();
    }

    /**
     * Metoda do zapisu informacji w pliku SharedPreferences
     * 
     * @param preferences
     *            nazwa pliku w ktorym zapisane sa dane
     * @param key
     *            dane do zapisu
     * @param value
     *            wartosc pod ktora zapisana jest dana informacja
     */
    public static void saveString(SharedPreferences preferences, String key,
	    String value) {
	SharedPreferences.Editor editor = preferences.edit();
	editor.putString(key, value);
	editor.commit();
    }

    /**
     * Metoda do zaladowania pliku ustawien
     * 
     * @param preferences
     *            plik ustawien, ktory ma byc zaladowany
     * 
     * @param key
     *            klucz, ktory chcemy zaladowac
     * 
     * @return uzyskany klucz
     */
    public static String loadString(SharedPreferences preferences, String key) {
	return preferences.getString(key, "");
    }

}
