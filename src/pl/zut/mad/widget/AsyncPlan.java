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
  // Tutaj mozesz zaimplenentowac czynno�ci, kt�re powinny zosta� zrealizowane po zakoczeniu operacji
	// Toast.makeText(/**?**/, "B��d", Toast.LENGTH_LONG).show();
 }
 
 @Override
 protected void onPreExecute() {
  // Analogicznie do metody onPostExecute, implenentujesz czynnosci do zrealizowania przed uruchomieniem w�tku
 }
 
 @Override
 protected void onProgressUpdate(Void... values) {
  // Natomiast metoda onProgressUpdate umozliwia aktualizwanie watku g��wnej podczas dzia�ana naszego Wymagaj�cegoWatku
  }
}