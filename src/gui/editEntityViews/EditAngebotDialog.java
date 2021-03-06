package gui.editEntityViews;

import gui.componentModels.EntityComboBoxModel;
import gui.componentModels.MyListCellRenderer;

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

import bl.BL;
import bl.objects.Angebot;
import bl.objects.Kunde;
import bl.objects.Projekt;
import dal.DALException;
import databinding.DataBinder;
import databinding.PercentRule;
import databinding.StandardRule;

public class EditAngebotDialog extends JDialog implements ActionListener {
	private JTextField[] textfeld;
	private JComboBox<Kunde> kunden;
	private JComboBox<Projekt> projekte;
	private JButton save, cancel;

	private Angebot a;

	private String[] columnNames = { "Beschreibung", "Summe", "Dauer",
			"Chance", "Kunde-ID", "Projekt-ID" };

	public EditAngebotDialog(JFrame owner) {
		super(owner, "Angebot hinzufuegen", true);
		this.a = null;
		initDialog();
	}

	public EditAngebotDialog(JFrame owner, Angebot a) {
		super(owner, "Angebot " + a.getAngebotID() + " bearbeiten", true);
		this.a = a;
		initDialog();
	}

	public void initDialog() {
		setSize(300, 250);
		setLocationRelativeTo(this.getOwner());
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

		save = new JButton("Save");
		cancel = new JButton("Cancel");

		save.addActionListener(this);
		cancel.addActionListener(this);

		panel.add(save);
		panel.add(cancel);

		return panel;
	}

	public JPanel initTextFields() {
		textfeld = new JTextField[columnNames.length - 2];

		JPanel panel = new JPanel(new GridLayout(columnNames.length, 2));

		for (int i = 0; i < textfeld.length; i++) {
			textfeld[i] = new JTextField(15);
			textfeld[i].setName(columnNames[i]);
			JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			JLabel l = new JLabel(columnNames[i]);
			p.add(l);
			panel.add(p);

			p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			p.add(textfeld[i]);

			panel.add(p);

		}

		kunden = new JComboBox<Kunde>();
		kunden.setRenderer(new MyListCellRenderer("nachname"));
		kunden.setName(columnNames[columnNames.length - 2]);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel l = new JLabel(columnNames[columnNames.length - 2]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(kunden);
		panel.add(p);

		projekte = new JComboBox<Projekt>();

		projekte.setRenderer(new MyListCellRenderer("name"));
		projekte.setName(columnNames[columnNames.length - 1]);
		p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		l = new JLabel(columnNames[columnNames.length - 1]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(projekte);
		panel.add(p);

		DataBinder d = new DataBinder();
		try {
			d.bindTo_int(projekte,
					new EntityComboBoxModel<Projekt>(BL.getProjektListe()),
					a == null ? -2 : a.getProjektID());
		} catch (DALException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		try {
			d.bindTo_int(kunden,
					new EntityComboBoxModel<Kunde>(BL.getKundeListe()),
					a == null ? -2 : a.getKundeID());
		} catch (DALException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
		if (a != null) {
			d.bindTo_String(textfeld[0], a.getBeschreibung());
			d.bindTo_double(textfeld[1], a.getSumme());
			d.bindTo_double(textfeld[2], a.getDauer());
			d.bindTo_double(textfeld[3], a.getChance());
		}

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == save) {
			try {
				DataBinder b = new DataBinder();
				String beschreibung = b.bindFrom_String(textfeld[0],
						new StandardRule());
				double summe = b.bindFrom_double(textfeld[1],
						new StandardRule());
				double dauer = b.bindFrom_double(textfeld[2],
						new StandardRule());
				double chance = b.bindFrom_double(textfeld[3],
						new PercentRule());
				int kundenID = b.bindFrom_int(kunden, null);
				int projektID = b.bindFrom_int(projekte, null);

				if (!b.hasErrors()) {
					if (a != null) {
						a.setBeschreibung(beschreibung);
						a.setSumme(summe);
						a.setDauer(dauer);
						a.setChance(chance);
						a.setKundeID(kundenID);
						a.setProjektID(projektID);
						BL.updateAngebot(a);
						// JOptionPane.showMessageDialog(this,
						// "Eintrag wurde erfolgreich bearbeitet");
					} else {
						a = new Angebot(beschreibung, summe, dauer, new Date(),
								chance, kundenID, projektID);
						BL.saveAngebot(a);
						// JOptionPane.showMessageDialog(this,
						// "Eintrag wurde erfolgreich hinzugef�gt");
					}
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, b.getErrors());
				}
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(this,
						"Kunde bzw. Projekt m�ssen ausgew�hlt sein");
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
