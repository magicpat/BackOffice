package gui;

import gui.tabbedViews.AngebotePanel;
import gui.tabbedViews.AusgangsrechnungenPanel;
import gui.tabbedViews.BuchungszeilenPanel;
import gui.tabbedViews.EingangsrechnungenPanel;
import gui.tabbedViews.KontaktPanel;
import gui.tabbedViews.KundenPanel;
import gui.tabbedViews.ProjektePanel;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class HauptFrame extends JFrame {
	private JTabbedPane reiter;

	public HauptFrame() {
		super("EPU - BackOffice");
		setSize(800, 600);
		// setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		reiter = new JTabbedPane();
		reiter.addTab("Kontakt", new KontaktPanel(this));
		reiter.addTab("Kunden", new KundenPanel(this));
		reiter.addTab("Projekte", new ProjektePanel(this));
		reiter.addTab("Angebote", new AngebotePanel(this));
		reiter.addTab("Ausgangsrechnungen", new AusgangsrechnungenPanel(this));
		reiter.addTab("Eingangsrechnungen", new EingangsrechnungenPanel(this));
		reiter.addTab("Bankkonto", new BuchungszeilenPanel(this));
		reiter.addTab("Berichte", new ReportTabbedPanel(this));

		add(reiter);

		setVisible(true);
	}

	public JTabbedPane getReiter() {
		return reiter;
	}
}
