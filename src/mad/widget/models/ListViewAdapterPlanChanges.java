package mad.widget.models;

import java.util.ArrayList;

import mad.widget.R;
import mad.widget.utils.Constans;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Wlasny adapter dla zmian w planie ListView
 * 
 * @author Sebastian Swierczek
 * @version 1.1.4
 */
public class ListViewAdapterPlanChanges extends
	ArrayAdapter<MessagePlanChanges> {

    /** Staly obiekt typu Context */
    private final Context context;

    /** ArrayList obiektow MessagePlanChanges */
    private ArrayList<MessagePlanChanges> messages = new ArrayList<MessagePlanChanges>();

    /**
     * Konstruktor klasy
     * 
     * @param context
     *            kontekst aplikacji
     * @param resource
     *            co zapisujemy
     * @param textViewResourceId
     *            id zasobu
     * @param objects
     *            do jakiej ArrayList zapisujemy
     */
    public ListViewAdapterPlanChanges(Context context, int resource,
	    int textViewResourceId, ArrayList<MessagePlanChanges> objects) {
	super(context, resource, textViewResourceId, objects);
	this.context = context;
	this.messages = objects;
    }

    /**
     * Metoda zwracajaca dane View
     * 
     * @param position
     *            indeks pozycji w danym View
     * @param convertView
     *            widok z ktorego wczytujemy
     * @param parent
     *            nadrzedne View
     * 
     * @return rowView zwraca View w postaci rzedu
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View rowView = inflater.inflate(R.layout.list_item, parent, false);
	TextView title = (TextView) rowView.findViewById(R.id.title);
	TextView body = (TextView) rowView.findViewById(R.id.body);
	TextView date = (TextView) rowView.findViewById(R.id.date);

	if (messages.get(position) != null) {
	    title.setTextColor(Color.WHITE);
	    int color = Color.argb(255, 17, 160, 213);
	    title.setBackgroundColor(color);

	    body.setTextColor(Color.BLACK);
	    body.setBackgroundColor(Color.WHITE);
	    body.setVisibility(View.GONE);
	    date.setTextColor(Color.BLACK);
	    date.setBackgroundColor(Color.WHITE);

	}

	String temp = messages.get(position).getTitle();
	temp = temp.substring(0, 1).toUpperCase()
		+ temp.substring(1, temp.length());

	if (temp.length() >= Constans.MAX_TITLE_LENGTH) {
	    temp = temp.substring(0, Constans.MAX_TITLE_LENGTH) + "...";
	    title.setText(temp);
	} else
	    title.setText(temp);

	Spanned sp = Html.fromHtml(messages.get(position).getBody().trim());
	temp = sp.toString().replaceAll("[\r\n]{1,}$", "");
	body.setText(temp);

	date.setText("Data: " + messages.get(position).getDate());

	return rowView;
    }
}
