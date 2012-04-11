package gui.models.tablemodels;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import bl.BL;
import bl.objects.Kunde;
import bl.objects.Projekt;

public class ProjektTableModel extends AbstractTableModel {
	private ArrayList<Projekt> projekte;
	private String[] columnNames = { "Projekt-ID", "Name", "Beschreibung" };

	public ProjektTableModel() {
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
		return projekte.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Projekt p = projekte.get(row);
		switch (col) {
		case 0:
			return p.getProjektID();
		case 1:
			return p.getName();
		case 2:
			return p.getBeschreibung();
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
		projekte = BL.getProjektListe();
		super.fireTableDataChanged();
	}
}
