package mad.widget.models;

import java.util.ArrayList;

import mad.widget.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * *
 * 
 * @author Sebastian Swierczek
 * @version 1.0.0
 */
public class ListViewAdapterWeekParity extends ArrayAdapter<DayParity> {

	/** Staly obiekt typu Context */
	private final Context context;

	ArrayList<DayParity> parityList = new ArrayList<DayParity>();

	public ListViewAdapterWeekParity(Context context, int resource,
			int textViewResourceId, ArrayList<DayParity> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.parityList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_item_week_parity, parent,
				false);

		TextView dateParity = (TextView) rowView.findViewById(R.id.dateParity);
		TextView parity = (TextView) rowView.findViewById(R.id.parity);
		String dateString = "";
		String parityString = "";
		String dayName = "";
		if (parityList.get(position) != null) {

			int ColorParzysty = Color.argb(255, 255, 0, 0);
			int ColorNieparzysty = Color.argb(255, 0, 0, 255);

			dateString = parityList.get(position).getDate();
			parityString = parityList.get(position).getParity();
			dayName = parityList.get(position).getDayName();
			
			if (parityString.equals("parzysty"))
				parity.setTextColor(ColorParzysty);

			else if (parityString.equals("nieparzysty"))
				parity.setTextColor(ColorNieparzysty);

		}

		dateParity.setText(dayName + " (" + dateString + ")");
		//dateParity.setText(dateString);
		parity.setText(parityString);

		return rowView;
	}
}
