package gui.models.tablemodels;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bl.BL;
import bl.objects.Angebot;
import dal.DALException;

public class AngebotTableModel extends AbstractTableModel {
	private ArrayList<Angebot> angebote;
	private String[] columnNames = { "Angebot-ID", "Summe", "Dauer", "Datum",
			"Chance", "Kunde-ID", "Projekt-ID" };

	public AngebotTableModel() {
		this.refresh();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return angebote.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Angebot a = angebote.get(row);
		switch (col) {
		case 0:
			return a.getAngebotID();
		case 1:
			return a.getSumme();
		case 2:
			return a.getDauer();
		case 3:
			return a.getDatumString();
		case 4:
			return a.getChance();
		case 5:
			return a.getKundeID();
		case 6:
			return a.getProjektID();
		default:
			return "";
		}
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void refresh() {
		try {
			angebote = BL.getAngebotsListe();
		} catch (DALException e) {
			System.out.println(e.getMessage());
		}
		super.fireTableDataChanged();
	}
}
