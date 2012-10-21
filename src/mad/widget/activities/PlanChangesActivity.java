package mad.widget.activities;

import java.util.ArrayList;

import mad.widget.R;
import mad.widget.connections.GetPlanChanges;
import mad.widget.connections.HttpConnect;
import mad.widget.models.ListViewAdapterPlanChanges;
import mad.widget.models.MessagePlanChanges;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Aktywnosc wyswietlajaca zmiany w planie w formie ListView
 * 
 * @author Sebastian Swierczek
 * @version 1.3.1
 */
public class PlanChangesActivity extends Activity {

    /** Obiekt typu GetPlanChanges */
    private final GetPlanChanges pars = new GetPlanChanges();

    /**
     * Zmienna do debuggowania.
     */
    private static final String TAG = "PlanChangesActivity";

    /**
     * Obiekt klasy Resources, odwolujacy sie do wartosci z pliku
     * res/strings.xml
     */
    private Resources res;

    /**
     * Obiekt ArrayList zawierajacy obiekty klasy MessagePlanChanges, gdzie
     * wyswietlane beda zmiany w planie
     */
    private ArrayList<MessagePlanChanges> news = new ArrayList<MessagePlanChanges>();

    /** Obiekt ListViewAdapterPlanChanges */
    private ListViewAdapterPlanChanges adapter;

    /** Obiekt ListView */
    private ListView lvPlanChanges;

    /** Obiekt ProgressDialog */
    private ProgressDialog pd;

    /** Zmienna stwierdzajaca wcisniecie przycisku odswiezania */
    private boolean enableExecuteRefresh = true;

    /** Metoda wywolywana przy starcie aktywnosci */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Log.i(TAG, "onCreate");
	setContentView(R.layout.main_plan_changes);
	res = getApplicationContext().getResources();

	if (HttpConnect.isOnline(getApplicationContext()) == true) {
	    // firstRun = false;
	    new AsyncTaskGetPlanChanges().execute(getApplicationContext());

	}
    }

    /**
     * Metoda wyswietlajaca powiadomienie Toast
     * 
     * @param text
     *            tekst powiadomienia
     * @param duration
     *            czas wyswietlania komunikatu
     * @param con
     *            kontekst aplikacji 
     */
    public void showToast(String text, int duration, Context con) {
	Log.i(TAG, "showToast");
	Toast.makeText(con, text, duration).show();
    }

    /** Metoda odswiezajaca ListView ze zmianami w planie */
    private void refreshListView() {
	Log.i(TAG, "refreshListView");
	lvPlanChanges = (ListView) findViewById(R.id.listPlanChanges);
	adapter = new ListViewAdapterPlanChanges(getApplicationContext(),
		android.R.layout.simple_list_item_1, android.R.id.text1, news);
	lvPlanChanges.setAdapter(adapter);
	adapter.notifyDataSetChanged();

	lvPlanChanges.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {

		TextView body = (TextView) view.findViewById(R.id.body);
		if (body.getVisibility() == View.GONE) {
		    body.setVisibility(View.VISIBLE);
		} else
		    body.setVisibility(View.GONE);
	    }
	});

	if (news.size() == 0) {
	    Toast.makeText(this, getString(R.string.no_messages),
		    Toast.LENGTH_LONG).show();
	}

    }

    /**
     * Metoda tworzaca menu opcji
     * 
     * @param menu
     * 
     * @return true, jezeli utworzono pomyslnie
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.plan_changes_menu, menu);
	return true;
    }

    /**
     * Metoda sprawdza wybor elementu z menu
     * 
     * @param item
     *            wybrany element menu
     * 
     * @return true, jezeli wybrano element
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	Log.i(TAG, "onOptionsItemSelected");
	switch (item.getItemId()) {
	case R.id.refresh:
	    if (enableExecuteRefresh) {
		if (HttpConnect.isOnline(getApplicationContext()) == true) {
		    new AsyncTaskGetPlanChanges()
			    .execute(getApplicationContext());
		}
	    }
	    return true;
	case R.id.exit:
	    this.finish();
	    return true;
	default:
	    return super.onOptionsItemSelected(item);
	}
    }

    /** Klasa pobierajaca zmiany w planie */
    private class AsyncTaskGetPlanChanges extends
	    AsyncTask<Context, Boolean, Void> {

	/**
	 * ArrayList obiektow MessagePlanChanges, gdzie beda przechowywane dane
	 * o zmianach w planie
	 */
	ArrayList<MessagePlanChanges> tempArray = null;

	/** Obiekt klasy Context */
	Context ctx;

	/** Wykonywanie zadan w tle watku glownego */
	@Override
	protected Void doInBackground(Context... params) {
	    Log.i(TAG, "doInBackground");
	    ctx = params[0];

	    if (HttpConnect.isOnline(getApplicationContext()) == true) {
		tempArray = pars.getServerMessages();
		if (tempArray != null) {
		    news = tempArray;
		} else {
		    publishProgress(false);

		}
	    }
	    return null;
	}

	/**
	 * Metoda umozliwia aktualizowanie watku glownego podczas dzialania
	 * PlanChangesActivity
	 */
	@Override
	protected void onProgressUpdate(Boolean... values) {
	    super.onProgressUpdate(values);
	    Log.i(TAG, "onProgressUpdate");
	    if (values[0] == false)
		showToast(res.getString(R.string.plan_changes_Messages), 3000,
			ctx);

	}

	/** Metoda wykonywana przed doInBackground() */
	@Override
	protected void onPreExecute() {
	    Log.i(TAG, "onPreExecute");
	    pd = ProgressDialog.show(PlanChangesActivity.this,
		    res.getString(R.string.plan_changes_refreshing_title),
		    res.getString(R.string.refreshing_body), true, true);
	    pd.setCancelable(false);
	    enableExecuteRefresh = false;

	}

	/** Metoda wykonywana po doInBackground() */
	@Override
	protected void onPostExecute(Void result) {
	    Log.i(TAG, "onPostExecute");
	    pd.dismiss();
	    if (tempArray != null) {
		refreshListView();
		publishProgress(true);
	    }
	    enableExecuteRefresh = true;
	}
    }

}
