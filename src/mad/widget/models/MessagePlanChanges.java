package mad.widget.models;

/**
 * Klasa w ktorej sa zawarte informacje o zmianach w planie
 */
public class MessagePlanChanges {

    /** Zmienna okreslajaca tytul */
    public String title;

    /** Zmienna okreslajaca date */
    public String date;

    /** Zmienna okreslajaca tresc */
    public String body;

    /** Konstruktor inicjalizujacy zmnienne */
    public MessagePlanChanges() {
	title = "";
	date = "";
	body = "";
    }

    /**
     * Metoda zwracajaca tytul
     * 
     * @return tytul wiadomosci
     */
    public String getTitle() {
	return title;
    }

    /**
     * Metoda zwracajaca date wiadomosci
     * 
     * @return data wiadomosci
     */
    public String getDate() {
	return date;
    }

    /**
     * Metoda zwracajaca tresc wiadomosci
     * 
     * @return tresc wiadomosci
     */
    public String getBody() {
	return body;
    }

    /**
     * Metoda ustawiajaca tytul wiadomosci
     * 
     * @param arg
     *            zadany tytul
     * */
    public void setTitle(String arg) {
	title = arg;
    }

    /**
     * Metoda ustawiajaca date wiadomosci
     * 
     * @param arg
     *            zadana data
     */
    public void setDate(String arg) {
	date = arg;
    }

    /**
     * Metoda ustawiajaca tresc wiadomosci
     * 
     * @param arg
     *            zadana tresc
     */
    public void setBody(String arg) {
	body = arg;
    }

}