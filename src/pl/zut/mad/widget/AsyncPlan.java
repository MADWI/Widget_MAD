package pl.zut.mad.widget;

import android.os.AsyncTask;

public class AsyncPlan extends AsyncTask<String, Void, String> {
 private String stronaPlanStacjonarne;
 @Override
 protected String doInBackground(String... params) {
	 TestHttpGet stronaPlan = new TestHttpGet();
	  	  
       try {
 			stronaPlanStacjonarne = stronaPlan.executeHttpGet();
 		} catch (Exception e) {
 		}
  return null;
 }
 
 @Override
 protected void onPostExecute(String result) {
  // Tutaj mozesz zaimplenentowac czynnoœci, które powinny zostaæ zrealizowane po zakoczeniu operacji
	// Toast.makeText(/**?**/, "B³¹d", Toast.LENGTH_LONG).show();
 }
 
 @Override
 protected void onPreExecute() {
  // Analogicznie do metody onPostExecute, implenentujesz czynnosci do zrealizowania przed uruchomieniem w¹tku
 }
 
 @Override
 protected void onProgressUpdate(Void... values) {
  // Natomiast metoda onProgressUpdate umozliwia aktualizwanie watku g³ównej podczas dzia³ana naszego Wymagaj¹cegoWatku
  }
}