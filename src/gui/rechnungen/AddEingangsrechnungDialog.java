package gui.rechnungen;

import gui.models.comboboxmodels.KontaktComboBoxModel;
import gui.models.comboboxmodels.MyListCellRenderer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InvalidObjectException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bl.BL;
import bl.objects.Ausgangsrechnung;
import bl.objects.Eingangsrechnung;
import bl.objects.Kontakt;
import bl.objects.Kunde;
import dal.DALException;

public class AddEingangsrechnungDialog extends JDialog implements
		ActionListener {
	private JComboBox<Kontakt> kontakt;
	private JComboBox<String> status;
	private JButton add, cancel;

	private String[] columnNames = { "Status", "Kontakt" };
	private String[] statusValues = { "offen", "bezahlt" };

	public AddEingangsrechnungDialog(JFrame owner) {
		super(owner, "Eingangsrechnung hinzufuegen", true);
		setSize(300, 150);
		setLocationRelativeTo(owner);
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

		add = new JButton("Add");
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
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel l = new JLabel(columnNames[0]);
		p.add(l);
		panel.add(p);
		
		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(status);
		panel.add(p);

		kontakt = new JComboBox<Kontakt>(new KontaktComboBoxModel(
				BL.getKontaktListe()));
		kontakt.setRenderer(new MyListCellRenderer("name"));
		p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		l = new JLabel(columnNames[1]);
		p.add(l);
		panel.add(p);

		p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(kontakt);
		panel.add(p);

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == add) {
			String[] inhalt = new String[columnNames.length];
			try {
				inhalt[0] = (String) status.getSelectedItem();
				inhalt[1] = String.valueOf(((Kontakt) (kontakt.getSelectedItem()))
						.getKontaktID());
				Eingangsrechnung a = new Eingangsrechnung(inhalt);
				BL.saveEingangsrechnung(a);
				dispose();
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(this,
						"Kontakt muss ausgew�hlt sein");
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
