package gui.rechnungen;

import gui.models.tablemodels.MyTableCellRenderer;
import gui.models.tablemodels.RechnungszeilenTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import dal.DALException;

import bl.BL;
import bl.objects.Angebot;
import bl.objects.Eingangsrechnung;
import bl.objects.Rechnungszeile;

public class RechnungszeilenDialog extends JDialog implements ActionListener {
	private JButton add, edit, delete, angebotInfo;
	private JTable table;
	private JScrollPane scrollpane;
	private RechnungszeilenTableModel tModel;
	private TableRowSorter<TableModel> tSorter;

	private JFrame owner;

	private int ausgangsrechnungsID, kundenID;

	public RechnungszeilenDialog(JFrame owner, int ausgangsrechnungsID,
			int kundenID) {
		super(owner, "EPU - Rechnungszeilen f�r RechnungsID "
				+ ausgangsrechnungsID, true);
		setSize(500, 300);
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.owner = owner;
		this.ausgangsrechnungsID = ausgangsrechnungsID;
		this.kundenID = kundenID;

		JPanel buttonPanel = initButtons();
		initTable();

		add(buttonPanel, BorderLayout.SOUTH);
		add(scrollpane);

		setVisible(true);
	}

	public JPanel initButtons() {
		add = new JButton("Add");
		edit = new JButton("Edit");
		delete = new JButton("Delete");
		angebotInfo = new JButton("Angebotinfo");

		add.addActionListener(this);
		edit.addActionListener(this);
		delete.addActionListener(this);
		angebotInfo.addActionListener(this);

		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(add);
		panel1.add(edit);
		panel1.add(delete);

		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel2.add(angebotInfo);

		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(panel1);
		panel.add(panel2);

		return panel;
	}

	public void initTable() {
		tModel = new RechnungszeilenTableModel(ausgangsrechnungsID);

		table = new JTable(tModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		for (String columnname : tModel.getColumnNames()) {
			table.getColumn(columnname).setCellRenderer(
					new MyTableCellRenderer());
		}

		tSorter = new TableRowSorter<TableModel>(table.getModel());
		tSorter.toggleSortOrder(0);
		tSorter.setSortsOnUpdates(true);

		table.setRowSorter(tSorter);

		scrollpane = new JScrollPane(table);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == add) {
			new EditRechnungszeileDialog(owner, ausgangsrechnungsID, kundenID);
			tModel.refresh();

		} else if (e.getSource() == delete) {
			int option = JOptionPane.showConfirmDialog(this,
					"Sollen die ausgew�hlten Elemente gel�scht werden?",
					"L�schauftrag", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				int[] a = table.getSelectedRows();
				for (int i = 0; i < a.length; i++) {
					int b = table.convertRowIndexToModel(a[i]);
					try {
						BL.deleteRechnungszeile((Integer) (tModel.getValueAt(b
								- i, 0)));
					} catch (DALException e1) {
						JOptionPane.showMessageDialog(this, e1.getMessage());
					}
				}
				tModel.refresh();
			}
		} else if (e.getSource() == edit) {
			int a = table.convertRowIndexToModel(table.getSelectedRow());
			Rechnungszeile r;
			try {
				r = BL.getRechnungszeile((Integer) tModel.getValueAt(a, 0));
				new EditRechnungszeileDialog(owner, ausgangsrechnungsID,
						kundenID, r);
				tModel.refresh();
			} catch (DALException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		} else if (e.getSource() == angebotInfo) {
			int a = table.convertRowIndexToModel(table.getSelectedRow());
			Angebot an;
			try {
				an = BL.getAngebot((Integer) tModel.getValueAt(a, 5));
				JOptionPane.showMessageDialog(this, an.toString());
			} catch (DALException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
	}
}
