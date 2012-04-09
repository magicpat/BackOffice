package bl.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import dal.DBEntity;

public class Kunde extends DBEntity {
	private int kundenID;
	private String vorname, nachname;
	private Date geburtsdatum;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	public Kunde(int id, String vorname, String nachname, Date geburtsdatum) {
		super();
		this.kundenID = id;
		this.vorname = vorname;
		this.nachname = nachname;
		this.geburtsdatum = geburtsdatum;
	}

	/**
	 * 
	 * @param inhalt
	 * @param inhalt
	 *            [0]=vorname;
	 * @param inhalt
	 *            [1]=nachname;
	 * @param inhalt
	 *            [2]=geburtsdatum;
	 * @throws ParseException
	 */
	public Kunde(String[] inhalt) throws ParseException,
			IllegalArgumentException {
		String exception = "";
		this.kundenID = -1;
		try {
			setVorname(inhalt[0]);
		} catch (IllegalArgumentException e) {
			exception += e.getMessage() + "\n";
		}
		try {
			setNachname(inhalt[1]);
		} catch (IllegalArgumentException e) {
			exception += e.getMessage() + "\n";
		}
		try {
			setGeburtsdatum(inhalt[2]);
		} catch (IllegalArgumentException e) {
			exception += e.getMessage() + "\n";
		}

		if (!exception.isEmpty()) {
			throw new IllegalArgumentException(exception);
		}

	}

	public int getId() {
		return kundenID;
	}

	public void setId(int id) {
		this.kundenID = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) throws IllegalArgumentException {
		if (vorname == null || vorname.isEmpty() || vorname == "") {
			throw new IllegalArgumentException("Vorname ist ung�ltig");
		}
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) throws IllegalArgumentException {
		if (nachname == null || nachname.isEmpty() || nachname == "") {
			throw new IllegalArgumentException("Nachname ist ung�ltig");
		}
		this.nachname = nachname;
	}

	public Date getGeburtsdatum() {
		return geburtsdatum;
	}

	public String getGeburtsdatumString() {
		return new StringBuilder(dateFormat.format(geburtsdatum)).toString();
	}

	public void setGeburtsdatum(Date geburtsdatum) {
		this.geburtsdatum = geburtsdatum;
	}

	public void setGeburtsdatum(String geburtsdatum) throws ParseException,
			IllegalArgumentException {
		if (geburtsdatum == null || geburtsdatum.isEmpty()) {
			throw new IllegalArgumentException("Geburtsdatum ist ung�ltig");
		}
		try {
			this.geburtsdatum = dateFormat.parse(geburtsdatum);
		} catch (ParseException e) {
			throw new ParseException("Datumsformat ung�ltig - (TT.MM.JJJJ)",
					e.getErrorOffset());
		}
	}

	public String toString() {
		return "Kunden-ID: " + kundenID + "\nName: " + vorname + " " + nachname
				+ "\nGeburtsdatum: " + getGeburtsdatumString();
	}

}
