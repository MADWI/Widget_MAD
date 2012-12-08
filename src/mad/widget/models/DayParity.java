package mad.widget.models;

/**
 * 
 * @author Sebastian Swierczek
 * @version 1.0.0
 */
public class DayParity {

	private String date;

	private String parity;

	private String dayName;

	public DayParity() {

		setDate("");
		setParity("");
	}

	public DayParity(String date, String parity, String dayName) {

		this.setDate(date);
		this.setParity(parity);
		this.setDayName(dayName);
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

}