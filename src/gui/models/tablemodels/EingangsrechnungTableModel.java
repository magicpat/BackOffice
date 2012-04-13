package gui.models.tablemodels;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import dal.DALException;

import bl.BL;
import bl.objects.Eingangsrechnung;

public class EingangsrechnungTableModel extends AbstractTableModel {
	private ArrayList<Eingangsrechnung> eingangsrechnungen;
	private String[] columnNames = { "Rechnung-ID", "Status", "Datum",
			"Kontakt-ID" };

	public EingangsrechnungTableModel() {
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
		return eingangsrechnungen.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Eingangsrechnung e = eingangsrechnungen.get(row);
		switch (col) {
		case 0:
			return e.getRechnungID();
		case 1:
			return e.getStatus();
		case 2:
			return e.getDatumString();
		case 3:
			return e.getKontaktID();
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
			eingangsrechnungen = BL.getEingangsrechnungenListe();
		} catch (DALException e) {
			System.out.println(e.getMessage());
		}
		super.fireTableDataChanged();
	}
}
