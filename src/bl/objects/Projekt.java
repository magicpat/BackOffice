package bl.objects;

import dal.DBEntity;
import dal.TableMeta;

@TableMeta(pkFieldName = "projektID")
public class Projekt extends DBEntity {
	private Integer projektID;
	private String name;
	private String beschreibung;

	public Projekt() {
	}

	public Projekt(String name, String beschreibung) {
		this.name = name;
		this.beschreibung = beschreibung;
	}

	public Projekt(Integer id, String name, String beschreibung) {
		this.projektID = id;
		this.name = name;
		this.beschreibung = beschreibung;
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

	@Override
	public String toString() {
		return "Projekt [projektID=" + projektID + ", name=" + name
				+ ", beschreibung=" + beschreibung + "]";
	}

	public String getValues() {
		return "Projekt-ID: " + projektID + "\nName: " + name
				+ "\nBeschreibung: " + beschreibung;
	}
}
