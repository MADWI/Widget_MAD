package pl.zut.mad.widget;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

/**
 * Klasa odpowiedzialan za pobieranie listy grup ze strony oraz pobieranie planu zajec dla kazdej grupy
 * @author Sebastian Peryt
 *
 */
public class WidgetDownload extends Activity { //nie wiedzialem jak to obejsc, zeby wykorzystac getResources
	
	//String stronaPlanStacjonarne;
	String urlPlanNiestacojonarne;
    
    /**
     * Funkcja pobiera zrdlo zadanej strony i zwraca w postaci stringa
     * @param string url strony do pobrania
     * @return string zrodlo pobranej strony
     * @author Sebastian Peryt
     */
	private String urlGet(String urlString){
        
        URLConnection urlConnection = null;
           URL url = null;
           String string = null;
            
           try {
      url = new URL(urlString);
      urlConnection = url.openConnection();
       
      InputStream inputStream = urlConnection.getInputStream();
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      BufferedReader reader = new BufferedReader(inputStreamReader);
       
          
      StringBuffer stringBuffer = new StringBuffer();
       
      while((string = reader.readLine()) != null){
       stringBuffer.append(string + "\n");
      }
      inputStream.close(); 
       
      string = stringBuffer.toString();
       
       
     } catch (MalformedURLException e) {
    	 Log.d("WidgetDownload", "Error: " + e);
     } catch (IOException e) {
    	 Log.d("WidgetDownload", "Error: " + e);
     }  
      
     return string;
       }
    
    /**
     * Funkcja na podstawie zadanego ciagu wejsciowego zwraca tablice z numerami grup.
     * @param String adres strony z ktorej ma byc pobrany plan (w wersji finalnej bedzie usuniety) 
     * @param String numer grupy np. I1 do wyszukania
     * @return Tablica stringow ze znalezionymi grupami
     * @author Sebastian Peryt
     */
	public String[] getGroups(String siteIn, String oznaczenie)
    { 
		HttpConnect con = new HttpConnect(10000, siteIn);
    	String site = null;
		//String[] outputTab = new String[]; //- Pamiec jest chyba dynamicznie przydzielana to co jest nie tak?
    	try{
    		site = con.getStrona();
    		Log.d("site","ok");
    	}
    	catch(Exception e)
    	{
    		Log.e("Error catch",e.toString());
    	}

    	if("" == site)
    	{
    		Log.e("Error", "Error con.getStrona()");
    	}	
    	Pattern p = Pattern.compile(">"+oznaczenie+"-[0-9]{2,3}\\.pdf<");
    	 Matcher m = p.matcher(site);
    	 int i = 0;
    	 while (m.find()) 
    	 { 
    		 i++;
    	 }
    	 m.reset();
    	 String[] outputTab = new String[i];
    	 i = 0;
    	 while (m.find()) 
    	 { 
    		 outputTab[i] = m.group().subSequence(1,m.group().indexOf(".pdf")).toString();
    		 //Log.d("WidgetDownload: ", outputTab[i]);
    		 i++;
    	 }
        return outputTab;
       }
    
	/**
	 * Funkcja sprawdza czy folder do przechowywania planu istnieje i ew. go tworzy
	 * @return true jesli folder istniej lub utworzono, false jesli wystapil blad
	 * @author Sebastian Peryt
	 */
    protected boolean setFolder()
    {
    	String newFolder = "/MAD_Plan_ZUT"; //nie wiem czemu nie dziala this.getString(R.string.folder_name);
    	//Log.d("folder", newFolder);
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File myNewFolder = new File(extStorageDirectory + newFolder);
        if(!myNewFolder.exists())//folder nie istnieje
        {
        	if(myNewFolder.mkdir())
        	{
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
    	return true;//folder istnieje
    }

	/**
	 * 
	 * @param String forma studiow - Stacjonarne, Niestacjonarne (Musi byc z wielkiej litery)
	 * @param String Pelny numer grupy dla korego ma zostac pobrany plan np. I1-22
	 * @return true jesli pomyslnie pobrano plan
	 * @author Sebastian Peryt
	 */
    public boolean downloadPlan(String forma, String grupa)
    {
    	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    	
    	try
    	{
    		WidgetDownload.this.setFolder();
    		Log.d("Info", "ok");
    	}
    	catch(Exception e)
    	{
    		Log.d("Info","Blad " + e);
    	}
    	if(setFolder())
    	{
    		Log.d("Folder: ", "utworzony");
    	}
    	else
    	{
    		Log.d("Folder: ", "juz byl");
    	}
    	
    	try {
    		URL url = new URL("http://wi.zut.edu.pl/plan/Wydruki/PlanGrup/" + forma + "/" + grupa + ".pdf");
    		File file = new File(extStorageDirectory + "/MAD_Plan_ZUT/" + grupa+".pdf");

            long startTime = System.currentTimeMillis();//Poczatek pobierania
            /* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

            /*
            * Define InputStreams to read from the URLConnection.
            */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            /*
            * Read bytes to the Buffer until there is nothing more to read(-1).
            */
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                   baf.append((byte) current);
            }

            /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            Log.d("WidgetDownload", "downloaded");//koniec pobierania
            return true;

            } catch (IOException e) {
                    Log.d("WidgetDownload", "Last Error: "+e);
                    return false;
            }
    }
}