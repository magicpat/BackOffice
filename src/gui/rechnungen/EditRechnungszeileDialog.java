package gui.rechnungen;

import gui.models.comboboxmodels.AngebotComboBoxModel;
import gui.models.comboboxmodels.MyListCellRenderer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InvalidObjectException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import dal.DALException;
import databinding.DataBinder;
import databinding.StandardRule;

import bl.BL;
import bl.objects.Angebot;
import bl.objects.Ausgangsrechnung;
import bl.objects.Projekt;
import bl.objects.Rechnungszeile;

public class EditRechnungszeileDialog extends JDialog implements ActionListener {
	private JTextField[] textfeld;
	private JButton add, cancel;
	private JComboBox<Angebot> angebote;

	private BL data;

	private DefaultTableModel tModel;
	private String[] columnNames = { "Rechnungs-ID", "Kommentar", "Steuersatz",
			"Betrag", "Angebot-ID" };
	private int rechnungID, kundenID;

	private Rechnungszeile r;

	public EditRechnungszeileDialog(JFrame owner, int rechnungID, int kundenID) {
		super(owner, "Rechnungszeile zur Ausgangsrechnung " + rechnungID
				+ " hinzufuegen", true);
		this.r = null;
		this.rechnungID = rechnungID;
		this.kundenID = kundenID;
		initDialog();
	}

	public EditRechnungszeileDialog(JFrame owner, int rechnungID, int kundenID,
			Rechnungszeile r) {
		super(owner, "Rechnungszeile zur Ausgangsrechnung " + rechnungID
				+ " hinzufuegen", true);
		this.r = r;
		this.rechnungID = rechnungID;
		this.kundenID = kundenID;
		initDialog();
	}

	public void initDialog() {
		setSize(320, 200);
		setLocationRelativeTo(getOwner());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel buttonPanel = initButtons();
		JPanel fields = initTextFields();

		add(buttonPanel, BorderLayout.SOUTH);
		add(fields);

		setVisible(true);
	}

	public JPanel initButtons() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		add = new JButton("Save");
		cancel = new JButton("Cancel");

		add.addActionListener(this);
		cancel.addActionListener(this);

		panel.add(add);
		panel.add(cancel);

		return panel;
	}

	public JPanel initTextFields() {
		textfeld = new JTextField[columnNames.length - 1];

		JPanel panel = new JPanel(new GridLayout(columnNames.length, 1));

		for (int i = 0; i < textfeld.length; i++) {
			textfeld[i] = new JTextField(15);
			textfeld[i].setName(columnNames[i]);
			if (columnNames[i].equals("Rechnungs-ID")) {
				textfeld[i].setText(String.valueOf(rechnungID));
				textfeld[i].setEditable(false);
			}
			JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JLabel l = new JLabel(columnNames[i]);
			p.add(l);
			panel.add(p);

			p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			p.add(textfeld[i]);

			panel.add(p);
		}

		try {
			angebote = new JComboBox<Angebot>(new AngebotComboBoxModel(
					BL.getAngebotsListe(kundenID)));
		} catch (DALException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		angebote.setName(columnNames[columnNames.length - 1]);
		angebote.setRenderer(new MyListCellRenderer("DatumString"));

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel l = new JLabel(columnNames[columnNames.length - 1]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(angebote);
		panel.add(p);

		if (r != null) {
			textfeld[1].setText(r.getKommentar());
			textfeld[1].setText(String.valueOf(r.getSteuersatz()));
			textfeld[2].setText(String.valueOf(r.getBetrag()));
			try {
				angebote.setSelectedItem(BL.getAngebot(r.getAngebotsID()));
			} catch (DALException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				System.exit(0);
			}
		}

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == add) {

			try {
				DataBinder b = new DataBinder();

				String kommentar = b.bindFrom_String(textfeld[1],
						new StandardRule());
				double steuersatz = b.bindFrom_double(textfeld[2],
						new StandardRule());
				double betrag = b.bindFrom_double(textfeld[3],
						new StandardRule());
				int angebotsID = b.bindFrom_int(angebote, null);

				if (!b.hasErrors()) {
					if (r != null) {
						r.setKommentar(kommentar);
						r.setSteuersatz(steuersatz);
						r.setBetrag(betrag);
						r.setAngebotsID(angebotsID);
						r.setRechnungID(rechnungID);
						BL.updateRechnungszeile(r);
					} else {
						r = new Rechnungszeile(-1, kommentar, steuersatz,
								betrag, rechnungID, angebotsID);
						BL.saveRechnungszeile(r);
					}
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, b.getErrors());
				}
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(this,
						"Angebot muss ausgew�hlt sein");
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage());
			} catch (InvalidObjectException ioe) {
				JOptionPane.showMessageDialog(this, ioe.getMessage());
			} catch (DALException de) {
				JOptionPane.showMessageDialog(this, de.getMessage());
			}
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}
}
