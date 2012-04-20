package bl.objects;

import dal.DBEntity;
import dal.TableMeta;

@TableMeta(pkFieldName = "projektID")
public class Projekt extends DBEntity {
	private Integer projektID;
	private String name;
	private String beschreibung;
	private Double verbrauchteStunden;

	public Projekt() {
	}

	public Projekt(String name, String beschreibung, Double verbrauchteStunden) {
		this.name = name;
		this.beschreibung = beschreibung;
		this.verbrauchteStunden = verbrauchteStunden;
	}

	public Projekt(Integer id, String name, String beschreibung,
			Double verbrauchteStunden) {
		this.projektID = id;
		this.name = name;
		this.beschreibung = beschreibung;
		this.verbrauchteStunden = verbrauchteStunden;
	}

	public Integer getProjektID() {
		return projektID;
	}

	public void setProjektID(Integer projektID) {
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

	public Double getVerbrauchteStunden() {
		return verbrauchteStunden;
	}

	public void setVerbrauchteStunden(Double verbrauchteStunden) {
		this.verbrauchteStunden = verbrauchteStunden;
	}

	@Override
	public String toString() {
		return "Projekt [projektID=" + projektID + ", name=" + name
				+ ", beschreibung=" + beschreibung + ", verbrauchteStunden="
				+ verbrauchteStunden + "]";
	}

	public String getValues() {
		return "Projekt-ID: " + projektID + "\nName: " + name
				+ "\nBeschreibung: " + beschreibung;
	}
}
