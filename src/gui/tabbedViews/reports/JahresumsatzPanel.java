package gui.tabbedViews.reports;

import gui.ReportViewPanel;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bl.BL;
import bl.objects.Projekt;
import bl.objects.view.reports.Jahresumsatz;
import dal.DALException;
import dal.DBEntity;

public class JahresumsatzPanel extends ReportViewPanel {
	private JMenuItem projektInfo;
	private JButton angebotsReport;

	private JTextField jahresUmsatz;

	public JahresumsatzPanel(JFrame owner) {
		super(Jahresumsatz.class, owner);

	}

	@Override
	public void initAdditionalButtons() {
		angebotsReport = new JButton("Als PDF speichern");
		JButton[] buttons = { angebotsReport };
		super.setAdditionalButtons(buttons);
	}

	@Override
	public void initAnalysisPanel() {
		jahresUmsatz = new JTextField(20);
		String[] labels = { "Prognostizierter Jahresumsatz:" };
		JTextField[] textfields = { jahresUmsatz };

		super.setAnalysisPanel(labels, textfields);
	}

	@Override
	public void refreshAnalysis() {
		double prognose = 0;
		for (DBEntity entry : tModel.getEntries()) {
			Jahresumsatz eintrag = (Jahresumsatz) entry;
			prognose += eintrag.getAvgAngebote();
		}
		jahresUmsatz.setText(String.valueOf(prognose));
	}

	@Override
	public void initPopupMenuItems() {
		projektInfo = new JMenuItem("Projektinfo");
		JMenuItem[] menuitems = { projektInfo };
		super.setPopupMenuItems(menuitems);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == projektInfo) {
			Jahresumsatz selectedItem = (Jahresumsatz) getSelectedItem();
			if (selectedItem != null) {
				try {
					Projekt p = BL.getProjekt(selectedItem.getProjektid());
					JOptionPane.showMessageDialog(this, p.toString());
				} catch (DALException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
			}
		}
	}

}
