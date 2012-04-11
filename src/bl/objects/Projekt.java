package bl.objects;

import dal.DBEntity;

public class Projekt extends DBEntity {
	private int projektID;
	private String name, beschreibung;

	public Projekt(int id, String name, String beschreibung) {
		this.projektID = id;
		this.name = name;
		this.beschreibung = beschreibung;
	}

	public Object getID() {
		return getProjektID();
	}

	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		if (beschreibung == null || beschreibung.isEmpty()) {
			throw new IllegalArgumentException("Beschreibung ist ung�ltig");
		}
		this.beschreibung = beschreibung;
	}

	public String toString() {
		return "Projekt-ID: " + projektID + "\nName: " + name
				+ "\nBeschreibung: " + beschreibung;
	}
}
