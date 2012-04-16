package gui.editEntityViews;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InvalidObjectException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bl.BL;
import bl.objects.Kunde;
import dal.DALException;
import databinding.BirthdayRule;
import databinding.DataBinder;
import databinding.StandardRule;

public class EditKundeDialog extends JDialog implements ActionListener {
	private JTextField[] textfeld;
	private JButton save, cancel;

	private Kunde k;

	private String[] columnNames = { "Vorname", "Nachname", "Geburtsdatum" };

	public EditKundeDialog(JFrame owner) {
		super(owner, "Kunde hinzuf�gen", true);
		this.k = null;

		initDialog();
	}

	public EditKundeDialog(JFrame owner, Kunde k) {
		super(owner, "Kunde " + k.getKundeID() + " bearbeiten", true);
		this.k = k;
		initDialog();
	}

	public void initDialog() {
		setSize(300, 200);
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

		save = new JButton("Save");
		cancel = new JButton("Cancel");

		save.addActionListener(this);
		cancel.addActionListener(this);

		panel.add(save);
		panel.add(cancel);

		return panel;
	}

	public JPanel initTextFields() {
		textfeld = new JTextField[columnNames.length];

		JPanel panel = new JPanel(new GridLayout(textfeld.length, 1));

		for (int i = 0; i < textfeld.length; i++) {
			textfeld[i] = new JTextField(20);
			textfeld[i].setName(columnNames[i]);
			JLabel l = new JLabel(columnNames[i]);

			JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			p.add(l);
			p.add(textfeld[i]);

			panel.add(p);
		}
		if (k != null) {
			textfeld[0].setText(k.getVorname());
			textfeld[1].setText(k.getNachname());
			textfeld[2].setText(k.getGeburtsdatumString());
		}

		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == save) {

			try {
				DataBinder b = new DataBinder();
				String vorname = b.bindFrom_String(textfeld[0],
						new StandardRule());
				String nachname = b.bindFrom_String(textfeld[1],
						new StandardRule());
				Date geburtsdatum = b.bindFrom_Date(textfeld[2],
						new BirthdayRule());

				if (!b.hasErrors()) {
					if (k != null) {
						k.setVorname(vorname);
						k.setNachname(nachname);
						k.setGeburtsdatum(geburtsdatum);
						BL.updateKunde(k);
						JOptionPane.showMessageDialog(this,
								"Eintrag wurde erfolgreich bearbeitet");
					} else {
						k = new Kunde(vorname, nachname, geburtsdatum);
						BL.saveKunde(k);
						JOptionPane.showMessageDialog(this,
								"Eintrag wurde erfolgreich hinzugef�gt");
					}
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, b.getErrors());
				}
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
