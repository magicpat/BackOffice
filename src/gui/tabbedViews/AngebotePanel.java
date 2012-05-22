package gui.tabbedViews;

import gui.EntityViewPanel;
import gui.editEntityViews.EditAngebotDialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bl.BL;
import bl.objects.Angebot;
import bl.objects.Kunde;
import bl.objects.Projekt;
import bl.objects.view.AngebotView;
import dal.DALException;

public class AngebotePanel extends EntityViewPanel {
	private JMenuItem kundenInfo, projektInfo;
	private JButton angebotsReport;

	// private JTextField jahresUmsatz;

	public AngebotePanel(JFrame owner) {
		super(Angebot.class, AngebotView.class, EditAngebotDialog.class, owner);
	}

	@Override
	public void initAdditionalButtons() {
		angebotsReport = new JButton("Angebotsreport (PDF)");
		JButton[] buttons = { angebotsReport };
		super.setAdditionalButtons(buttons);
	}

	@Override
	public void initAnalysisPanel() {
		// JPanel analysisPanel = new JPanel(new GridLayout(2, 1));
		//
		// analysisPanel.add(new JLabel("Jahresumsatz"));
		// jahresUmsatz = new JTextField();
		// jahresUmsatz.setEnabled(false);
		// jahresUmsatz.setEditable(false);
		// jahresUmsatz.setHorizontalAlignment(JTextField.CENTER);
		// analysisPanel.add(jahresUmsatz);
		//
		// super.setAnalysisPanel(analysisPanel);
	}

	@Override
	public void initPopupMenuItems() {
		kundenInfo = new JMenuItem("Kundeninfo");
		projektInfo = new JMenuItem("Projektinfo");
		JMenuItem[] menuitems = { kundenInfo, projektInfo };
		super.setPopupMenuItems(menuitems);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == kundenInfo) {
			Angebot selectedItem = (Angebot) getSelectedDBEntity();
			if (selectedItem != null) {
				try {
					Kunde k = BL.getKunde(selectedItem.getKundeID());
					JOptionPane.showMessageDialog(this, k.toString());
				} catch (DALException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
			}
		} else if (e.getSource() == projektInfo) {
			Angebot selectedItem = (Angebot) getSelectedDBEntity();
			if (selectedItem != null) {
				try {
					Projekt p = BL.getProjekt(selectedItem.getProjektID());
					JOptionPane.showMessageDialog(this, p.toString());
				} catch (DALException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
			}
		}
	}

}
