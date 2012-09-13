package mad.widget.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {

	public static SharedPreferences getSharedPreferences(Context ctx) {
		return ctx.getSharedPreferences(Constans.PREFERENCES_NAME,
				Activity.MODE_PRIVATE);
	}

	public static void clearPreferences(Context ctx) {
		SharedPreferences preferences = ctx.getSharedPreferences(
				Constans.PREFERENCES_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	public static void saveString(SharedPreferences preferences, String key,
			String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String loadString(SharedPreferences preferences, String key) {
		return preferences.getString(key, "");
	}

}
