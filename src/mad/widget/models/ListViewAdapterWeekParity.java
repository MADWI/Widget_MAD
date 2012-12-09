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
		TextView separator = (TextView) rowView.findViewById(R.id.separator);

		int color = Color.argb(255, 17, 160, 213);
		separator.setBackgroundColor(color);

		String dateString = "";
		String parityString = "";
		String dayName = "";
		if (parityList.get(position) != null) {

			dateString = parityList.get(position).getDate();
			parityString = parityList.get(position).getParity();
			dayName = parityList.get(position).getDayName();

			if (parityString.equals("parzysty")) {
				parity.setTextColor(Color.RED);
				dateParity.setTextColor(Color.BLACK);

			}

			else if (parityString.equals("nieparzysty")) {
				parity.setTextColor(Color.BLUE);
				dateParity.setTextColor(Color.BLACK);
			} else {
				parity.setTextColor(Color.GRAY);
				dateParity.setTextColor(Color.BLACK);
			}

		}

		if (dayName.equals(context.getString(R.string.first_day_of_week))) {
			separator.setText(context.getString(R.string.new_week_separator));
			separator.setVisibility(View.VISIBLE);
		} else
			separator.setVisibility(View.GONE);

		dateParity.setText(dayName + " (" + dateString + ")");
		// dateParity.setText(dateString);
		parity.setText(parityString);

		return rowView;
	}
}
