package gui.editEntityViews;

import gui.componentModels.EntityComboBoxModel;
import gui.componentModels.MyListCellRenderer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InvalidObjectException;
import java.text.SimpleDateFormat;
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
import bl.objects.Eingangsrechnung;
import bl.objects.Kontakt;
import dal.DALException;
import databinding.DataBinder;
import databinding.StandardRule;

public class EditEingangsrechnungDialog extends JDialog implements
		ActionListener {
	private JComboBox<Kontakt> kontakt;
	private JComboBox<String> status;
	private JTextField datum;
	private JButton add, cancel;

	private String[] columnNames = { "Status", "Datum", "Kontakt" };
	private String[] statusValues = { "offen", "bezahlt" };

	private Eingangsrechnung er;

	public EditEingangsrechnungDialog(JFrame owner) {
		super(owner, "Eingangsrechnung hinzufuegen", true);
		this.er = null;
		initDialog();
	}

	public EditEingangsrechnungDialog(JFrame owner, Eingangsrechnung er) {
		super(owner, "Eingangsrechnung " + er.getRechnungID() + " bearbeiten",
				true);
		this.er = er;
		initDialog();

	}

	public void initDialog() {
		setSize(300, 150);
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

		JPanel panel = new JPanel(new GridLayout(columnNames.length, 1));

		status = new JComboBox<String>(statusValues);
		status.setName(columnNames[0]);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel l = new JLabel(columnNames[0]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(status);
		panel.add(p);

		datum = new JTextField(15);
		datum.setName(columnNames[1]);
		p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		l = new JLabel(columnNames[1]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(datum);
		panel.add(p);

		try {
			kontakt = new JComboBox<Kontakt>(new EntityComboBoxModel<Kontakt>(
					BL.getKontaktListe()));
		} catch (DALException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			System.exit(0);
		}
		kontakt.setName(columnNames[2]);
		kontakt.setRenderer(new MyListCellRenderer("firma"));
		p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		l = new JLabel(columnNames[2]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(kontakt);
		panel.add(p);

		if (er != null) {
			status.setSelectedItem(er.getStatus());
			for (int i = 0; i < kontakt.getItemCount(); i++) {
				if (kontakt.getItemAt(i).getKontaktID() == er.getKontaktID()) {
					kontakt.setSelectedIndex(i);
					break;
				}
			}
			datum.setText(er.getDatumString());
		} else {
			datum.setText(new StringBuilder(new SimpleDateFormat("dd.MM.yyyy")
					.format(new Date())).toString());
		}

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == add) {
			try {
				DataBinder b = new DataBinder();
				String status = b.bindFrom_String2(this.status, null);
				Date datum = b.bindFrom_Date(this.datum, new StandardRule());
				int kontaktID = b.bindFrom_int(kontakt, null);

				if (!b.hasErrors()) {
					if (er != null) {
						er.setStatus(status);
						er.setDatum(datum);
						er.setKontaktID(kontaktID);
						BL.updateEingangsrechnung(er);
						JOptionPane.showMessageDialog(this,
								"Eintrag wurde erfolgreich bearbeitet");
					} else {
						er = new Eingangsrechnung(status, datum, kontaktID);
						BL.saveEingangsrechnung(er);
						JOptionPane.showMessageDialog(this,
								"Eintrag wurde erfolgreich hinzugef�gt");
					}
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, b.getErrors());
				}
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(this,
						"Kontakt muss ausgew�hlt sein");
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage());
			} catch (InvalidObjectException ioe) {
				JOptionPane.showMessageDialog(this, ioe.getMessage());
			} catch (DALException de) {
				de.printStackTrace();
				JOptionPane.showMessageDialog(this, de.getMessage());
			}
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}
}
