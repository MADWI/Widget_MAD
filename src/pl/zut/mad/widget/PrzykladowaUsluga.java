package pl.zut.mad.widget;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class PrzykladowaUsluga extends Service {
	 private Looper mServiceLooper;
	  private ServiceHandler mServiceHandler;

	 //uchwyt ktory odbiera wiaodmosci od watku
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {

	    	  //TUTAJ WYKONUJEMY ZADANIE USLGU, POCZEKAM PO PROSTU 5 SEKUND
	    	  TestHttpGet stronaPlan = new TestHttpGet();
	    	  String stronaPlanStacjonarne;
	    	  
		        try {
		  			stronaPlanStacjonarne = stronaPlan.executeHttpGet();
		  			Toast.makeText(getApplicationContext(), stronaPlanStacjonarne, Toast.LENGTH_LONG).show();
		  		} catch (Exception e) {
		  			Toast.makeText(getApplicationContext(), "B³¹d", Toast.LENGTH_LONG).show();
		  		}
	          //zatrzymujemy usluge odebirajac startId  w onCreate() ->   msg.arg1 = startId;
	          stopSelf(msg.arg1);
	      }
	  }

	  @Override
	  public void onCreate() {
	    
		//tworzymy watek aby nie pracowac w watku gopodarza
		HandlerThread thread = new HandlerThread("ServiceStartArguments");
	    thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    mServiceLooper = thread.getLooper();
	    mServiceHandler = new ServiceHandler(mServiceLooper);
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
	      

	      //dla kazdego ¿adania uruchomienia uslugi wysylaly wiadomosc aby wystartowac prace
	      //oraz dostarczyc id startu, dzieki koremu wiemy ze mamy zakonczyc wykonana prace
	   
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      mServiceHandler.sendMessage(msg);
	      
	      return START_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	      // to nie jest usluga zwiazana wiec zwracamy po prostu null
	      return null;
	  }

	  @Override
	  public void onDestroy() {
	    //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
	  }
	}

