package mad.widget.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mad.widget.R;
import mad.widget.connections.PlanDownloader;
import mad.widget.utils.Constans;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RemovePlans extends Activity implements OnClickListener {

	private List<String> fileList = new ArrayList<String>();
	private File root = new File(Environment.getExternalStorageDirectory()
			+ Constans.PLAN_FOLDER);
	private ListView listPlans;
	private Button removePlans;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_plans);

		listPlans = (ListView) findViewById(R.id.listPlany);
		removePlans = (Button) findViewById(R.id.btnUsunPlany);
		removePlans.setOnClickListener(this);

		ListDir(root);

	}

	private void ListDir(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			fileList.clear();
			for (File file : files) {
				fileList.add(file.getPath());
			}

			ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, fileList);
			listPlans.setAdapter(directoryList);
			listPlans.setClickable(true);
			listPlans.setOnItemClickListener(myClickListener);
		}

	}

	public OnItemClickListener myClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				final int position, long arg3) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					RemovePlans.this);

			// set title
			alertDialogBuilder
					.setTitle(getString(R.string.remove_plan_alert_dialog));

			// set dialog message
			alertDialogBuilder
					.setMessage(R.string.remove_plan_message)
					.setCancelable(false)
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									PlanDownloader.removePlan(fileList
											.get(position));
									ListDir(root);
								}
							})
					.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnUsunPlany:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					RemovePlans.this);

			// set title
			alertDialogBuilder
					.setTitle(getString(R.string.remove_plans_alert_dialog));

			// set dialog message
			alertDialogBuilder
					.setMessage(R.string.remove_plans_message)
					.setCancelable(false)
					.setPositiveButton("Tak",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									PlanDownloader.removePlans();
									finish();
								}
							})
					.setNegativeButton("Nie",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

			break;
		}
	}
}
