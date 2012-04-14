package gui.kontakte;

import gui.models.tablemodels.KontaktTableModel;
import gui.models.tablemodels.MyTableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import bl.BL;
import bl.objects.Kontakt;
import dal.DALException;

public class KontaktPanel extends JPanel implements ActionListener {
	private JButton add, edit, delete, angebote, search, refresh;
	private JTextField searchField;
	private JTable table;
	private JScrollPane scrollpane;
	private KontaktTableModel tModel;
	private TableRowSorter<TableModel> tSorter;

	private JFrame owner;

	public KontaktPanel(JFrame owner) {
		// super("EPU - Kunden");
		setSize(600, 300);
		// setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		// setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.owner = owner;

		JPanel buttonPanel = initButtons();
		initTable();

		add(buttonPanel, BorderLayout.WEST);
		add(scrollpane);

		setVisible(true);
	}

	public JPanel initButtons() {
		add = new JButton("Add");
		edit = new JButton("Edit");
		delete = new JButton("Delete");
		angebote = new JButton("Show Angebote");
		search = new JButton("Show Filter");
		refresh = new JButton("Show All");
		searchField = new JTextField();

		add.addActionListener(this);
		edit.addActionListener(this);
		delete.addActionListener(this);
		angebote.addActionListener(this);
		refresh.addActionListener(this);
		search.addActionListener(this);
		searchField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
					search.doClick();
				}
			}
		});

		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(add);
		panel1.add(edit);
		panel1.add(delete);

		JPanel panel2 = new JPanel(new GridLayout(1, 1));
		panel2.add(angebote);

		JPanel panel3 = new JPanel(new GridLayout(4, 1));
		panel3.add(new JLabel("<html><body><b> Filter:</b></body></html>"));
		panel3.add(searchField);
		panel3.add(search);
		panel3.add(refresh);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		panel.add(panel1, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 2;
		panel.add(panel3, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridy = 1;
		/** LAST ELEMENT **/
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1.0;
		/** LAST ELEMENT **/
		panel.add(panel2, gbc);

		//
		// JButton button3 = new JButton("3");
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.gridy = 2;
		// panel.add(button3, gbc);
		//
		// JButton button4 = new JButton("4");
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.gridy = 3;
		// panel.add(button4, gbc);
		//
		// //und so weiter...
		// JButton button5 = new JButton("..");
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.gridy = 4;
		// panel.add(button5, gbc);
		//
		// JButton button20 = new JButton("20");
		// gbc.fill = GridBagConstraints.HORIZONTAL;
		// gbc.anchor = GridBagConstraints.NORTH;
		// gbc.weighty = 1.0;
		// gbc.gridy = 6;
		// panel.add(button20, gbc);

		return panel;
	}

	public void initTable() {
		// tModel = new DefaultTableModel(rows, columnNames);
		tModel = new KontaktTableModel();
		table = new JTable(tModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		for (String columnname : tModel.getColumnNames()) {
			table.getColumn(columnname).setCellRenderer(
					new MyTableCellRenderer());
		}
		table.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_F5) {
					tModel.refresh();
				}
			}
		});

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
			new EditKontaktDialog(owner);
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
					System.out.println(b);
					try {
						BL.deleteKontakt(Integer.valueOf(String.valueOf(tModel
								.getValueAt(b, 0))));
					} catch (DALException e1) {
						JOptionPane.showMessageDialog(this, e1.getMessage());
					}
				}
				tModel.refresh();
			}
		} else if (e.getSource() == edit) {
			int a = table.convertRowIndexToModel(table.getSelectedRow());
			Kontakt k;
			try {
				k = BL.getKontakt(Integer.valueOf(String.valueOf(tModel
						.getValueAt(a, 0))));
				new EditKontaktDialog(owner, k);
				tModel.refresh();
			} catch (DALException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		} else if (e.getSource() == search) {
			if (!searchField.getText().isEmpty()) {
				tModel.setFilter(searchField.getText());
				tModel.refresh();
			}
		} else if (e.getSource() == refresh) {
			searchField.setText("");
			tModel.setFilter("");
			tModel.refresh();
		}
	}

}
