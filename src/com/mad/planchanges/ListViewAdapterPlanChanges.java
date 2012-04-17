/*
 * Sebastian Swierczek
 * The West Pomeranian University of Technology
 * sswierczek@wi.zut.edu.pl
 */
package com.mad.planchanges;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListViewAdapterPlanChanges extends ArrayAdapter<DataPlanChanges> {

	private static final int MAX_TITLE_LENGHT = 35;
	private static final int MAX_BODY_LENGHT = 100;
	private final Context context;
	private ArrayList<DataPlanChanges> messages = new ArrayList<DataPlanChanges>();
	
	public ListViewAdapterPlanChanges(Context context, int resource,
			int textViewResourceId, ArrayList<DataPlanChanges> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.messages = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_item, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.title);
		TextView body = (TextView) rowView.findViewById(R.id.body);
		TextView date = (TextView) rowView.findViewById(R.id.date);

		String temp = messages.get(position).getTitle();
		
		if (temp.length() >= MAX_TITLE_LENGHT) {
			temp = temp.substring(0, MAX_TITLE_LENGHT) + "...";
			
			title.setText(temp);
		} else
			title.setText(messages.get(position).getTitle());		
		
		temp = messages.get(position).getBody();
				
		if (temp.length() >= MAX_BODY_LENGHT) {
			temp = temp.substring(0, MAX_BODY_LENGHT) + "...";
			body.setText(temp);
		} else
			body.setText(temp);
		
		date.setText(messages.get(position).getDate());
		
		return rowView;
	}
}
