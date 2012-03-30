package bl;

import java.io.InvalidObjectException;
import java.util.ArrayList;

import dal.DALException;

import bl.models.armin.*;

public class BL {
	private static ArrayList<Projekt> projektliste;
	private static int projektID = 0;
	private static ArrayList<Kunde> kundenliste;
	private static int kundenID = 0;
	private static ArrayList<Angebot> angebotsliste;
	private static int angebotID = 0;
	private static ArrayList<Ausgangsrechnung> ausgangsrechnungenliste;
	private static int rechnungID = 0;
	private static ArrayList<Rechnungszeile> rechnungszeilenliste;
	private static int rechnungszeileID = 0;

	public BL() {
		projektliste = new ArrayList<Projekt>();
		kundenliste = new ArrayList<Kunde>();
		angebotsliste = new ArrayList<Angebot>();
		ausgangsrechnungenliste = new ArrayList<Ausgangsrechnung>();
		rechnungszeilenliste = new ArrayList<Rechnungszeile>();

	}

	public static ArrayList<Kunde> getKundenListe() throws DALException {
		return kundenliste;
	}

	public static Kunde getKunde(int kundenID) throws DALException {
		for (int i = 0; i < kundenliste.size(); i++) {
			if (kundenliste.get(i).getId() == kundenID) {
				return kundenliste.get(i);
			}
		}
		throw new DALException("Kunden-ID nicht vorhanden");
	}

	public static void deleteKunde(int kundenID) throws DALException {
		Kunde k = getKunde(kundenID);
		if (k != null) {
			kundenliste.remove(k);
		}
	}

	public static void saveKunde(Kunde k) throws DALException,
			InvalidObjectException {
		String exception = "";
		// ...
		if (!exception.isEmpty()) {
			throw new InvalidObjectException(exception);
		}

		k.setId(kundenID++);
		kundenliste.add(k);
	}

	public static ArrayList<Projekt> getProjektListe() throws DALException {
		return projektliste;
	}

	public static Projekt getProjekt(int projektID) throws DALException {
		for (int i = 0; i < projektliste.size(); i++) {
			if (projektliste.get(i).getId() == projektID) {
				return projektliste.get(i);
			}
		}
		throw new DALException("Projekt-ID nicht vorhanden");
	}

	public static void deleteProjekt(int projektID) throws DALException {
		Projekt p = getProjekt(projektID);
		if (p != null) {
			projektliste.remove(p);
		}
	}

	public static void saveProjekt(Projekt p) throws DALException,
			InvalidObjectException {
		String exception = "";
		// ...
		if (!exception.isEmpty()) {
			throw new InvalidObjectException(exception);
		}

		p.setId(projektID++);
		projektliste.add(p);
	}

	public static ArrayList<Angebot> getAngebotsListe() throws DALException {
		return angebotsliste;
	}

	public static ArrayList<Angebot> getAngebotsListe(int kundenID)
			throws DALException {
		ArrayList<Angebot> ret = new ArrayList<Angebot>();
		for (Angebot a : angebotsliste) {
			if (a.getKundenID() == kundenID) {
				ret.add(a);
			}
		}
		return ret;
	}

	public static Angebot getAngebot(int angebotID) throws DALException {
		for (int i = 0; i < angebotsliste.size(); i++) {
			if (angebotsliste.get(i).getId() == angebotID) {
				return angebotsliste.get(i);
			}
		}
		throw new DALException("Angebot-ID nicht vorhanden");
	}

	public static void deleteAngebot(int angebotID) throws DALException {
		Angebot a = getAngebot(angebotID);
		if (a != null) {
			projektliste.remove(a);
		}
	}

	public static void saveAngebot(Angebot a) throws DALException,
			InvalidObjectException {
		String exception = "";
		// ... Kunden bzw Projekt-ID �berpr�fen
		if (!exception.isEmpty()) {
			throw new InvalidObjectException(exception);
		}

		a.setId(angebotID++);
		angebotsliste.add(a);
	}

	public static ArrayList<Ausgangsrechnung> getAusgangsrechnungenListe()
			throws DALException {
		return ausgangsrechnungenliste;
	}

	public static Ausgangsrechnung getAusgangsrechnung(int rechnungID)
			throws DALException {
		for (int i = 0; i < ausgangsrechnungenliste.size(); i++) {
			if (ausgangsrechnungenliste.get(i).getId() == rechnungID) {
				return ausgangsrechnungenliste.get(i);
			}
		}
		throw new DALException("Rechnung-ID nicht vorhanden");
	}

	public static void deleteAusgangsrechnung(int rechnungID)
			throws DALException {
		Ausgangsrechnung a = getAusgangsrechnung(rechnungID);
		if (a != null) {
			ausgangsrechnungenliste.remove(a);
		}
	}

	public static void saveAusgangsrechnung(Ausgangsrechnung a)
			throws DALException, InvalidObjectException {
		String exception = "";
		// ... Kunden-ID �berpr�fen
		if (!exception.isEmpty()) {
			throw new InvalidObjectException(exception);
		}

		a.setId(rechnungID++);
		ausgangsrechnungenliste.add(a);
	}

	public static ArrayList<Rechnungszeile> getRechnungszeilenListe()
			throws DALException {
		return rechnungszeilenliste;
	}

	public static Rechnungszeile getRechnungszeile(int rechnungszeileID)
			throws DALException {
		for (int i = 0; i < rechnungszeilenliste.size(); i++) {
			if (rechnungszeilenliste.get(i).getId() == rechnungszeileID) {
				return rechnungszeilenliste.get(i);
			}
		}
		throw new DALException("Rechnungszeile-ID nicht vorhanden");
	}

	public static void deleteRechnungszeile(int rechnungszeileID)
			throws DALException {
		Rechnungszeile r = getRechnungszeile(rechnungszeileID);
		if (r != null) {
			rechnungszeilenliste.remove(r);
		}
	}

	public static void saveRechnungszeile(Rechnungszeile r)
			throws DALException, InvalidObjectException {
		String exception = "";
		// ... rechnung bzw angebot-ID �berpr�fen
		if (!exception.isEmpty()) {
			throw new InvalidObjectException(exception);
		}

		r.setId(rechnungszeileID++);
		rechnungszeilenliste.add(r);
	}

}
