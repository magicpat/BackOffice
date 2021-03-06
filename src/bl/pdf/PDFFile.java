package bl.pdf;

import gui.componentModels.EntityTableModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import bl.BL;
import bl.objects.Ausgangsrechnung;
import bl.objects.Eingangsrechnung;
import bl.objects.Kontakt;
import bl.objects.Kunde;
import bl.objects.Rechnung;
import bl.objects.Rechnungszeile;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import dal.DALException;
import dal.DBEntity;

public class PDFFile {
	private Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	private Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);
	private Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

	private Document document;

	public void open() {
		if (document != null) {
			document.open();
		}
	}

	public void close() {
		if (document != null) {
			if (document.isOpen()) {
				document.close();
			}
		}
	}

	// public static void main(String[] args) {
	// try {
	// Document document = new Document();
	// PdfWriter.getInstance(document, new FileOutputStream(FILE));
	// document.open();
	// addMetaData(document);
	// addTitlePage(document);
	// addContent(document);
	// document.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public PDFFile(File file) throws DocumentException, IOException {
		document = new Document();
		if (!file.exists()) {
			file = new File(file.getPath() + ".pdf");
			file.createNewFile();
		}
		PdfWriter.getInstance(document, new FileOutputStream(file));
	}

	// iText allows to add metadata to the PDF which can be viewed in your Adobe
	// Reader
	// under File -> Properties
	private void addMetaData() {
		document.open();
		document.addTitle("My first PDF");
		document.addSubject("Using iText");
		document.addKeywords("Java, PDF, iText");
		document.addAuthor("Lars Vogel");
		document.addCreator("Lars Vogel");
		document.close();
	}

	private Paragraph newLine(String txt, Font f, int alignment) {
		Paragraph ret;
		if (f != null) {
			ret = new Paragraph(txt, f);
		} else {
			ret = new Paragraph(txt);
		}
		if (alignment != -1) {
			ret.setAlignment(alignment);
		}
		return ret;
	}

	public void createReport(String titel, EntityTableModel tModel) throws DocumentException {
		document.open();
		Paragraph preface = new Paragraph();

		preface.add(newLine(titel, catFont, -1));
		addEmptyLine(preface, 2);
		
		preface.add(getTable(tModel));
		
		document.add(preface);
		// Start a new page
		document.newPage();
		document.close();
	}

	private PdfPTable getTable(EntityTableModel tModel) {
		String[] headers = tModel.getColumnNames();

		PdfPTable table = new PdfPTable(headers.length);
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		for (String header : headers) {
			PdfPCell c1 = new PdfPCell(new Phrase(header));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
		}

		ArrayList<DBEntity> entries = tModel.getEntries();
		if (entries.isEmpty()) {
			PdfPCell c = new PdfPCell(new Phrase("Keine Eintr�ge vorhanden"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			c.setColspan(headers.length);
			table.addCell(c);
			return table;
		}
		for (DBEntity entry : entries) {
			for (String header : headers) {
				Method method;

				Object a;
				try {
					method = entry.getClass()
							.getMethod("get"+header, new Class<?>[0]);
					a = method.invoke(entry, new Object[0]);
					table.addCell(String.valueOf(a));
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					table.addCell(new String(""));
				} catch (SecurityException e) {
					e.printStackTrace();
					table.addCell(new String(""));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					table.addCell(new String(""));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					table.addCell(new String(""));
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					table.addCell(new String(""));
				}
			}
		}
		return table;
	}

	public void createRechnung(Rechnung r) throws DocumentException,
			DALException {
		document.open();
		Paragraph preface = new Paragraph();

		preface.add(newLine(new Date().toString(), small, Element.ALIGN_RIGHT));

		// Lets write a big header
		preface.add(newLine("Rechnung", catFont, -1));
		preface.add(newLine(r.getValues(), small, Element.ALIGN_LEFT));

		// Will create: Report generated by: _name, _date
		String to = "";
		if (r instanceof Ausgangsrechnung) {
			Kunde k = BL.getKunde(((Ausgangsrechnung) r).getKundeID());
			to += k.getValues();
		} else if (r instanceof Eingangsrechnung) {
			Kontakt k = BL.getKontakt(((Eingangsrechnung) r).getKontaktID());
			to += k.getValues();
		}
		preface.add(newLine(to, smallBold, Element.ALIGN_RIGHT));

		// We add one empty line
		addEmptyLine(preface, 2);

		// add table
		preface.add(getRechnungszeileTable(r));

		document.add(preface);
		// Start a new page
		document.newPage();
		document.close();
	}

	private PdfPTable getRechnungszeileTable(Rechnung r) throws DALException {
		String[] headers;
		if (r instanceof Eingangsrechnung) {
			String[] h = { "Rechnungszeile", "Kommentar", "Steuersatz",
					"Betrag", "ohne Steuer" };
			headers = h;
		} else {
			String[] h = { "Rechnungszeile", "Kommentar", "AngebotID",
					"Steuersatz", "Betrag", "ohne Steuer" };
			headers = h;
		}
		PdfPTable table = new PdfPTable(headers.length);
		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		for (String header : headers) {
			PdfPCell c1 = new PdfPCell(new Phrase(header));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);
		}

		ArrayList<Rechnungszeile> rechnungszeilen = BL.getRechnungszeileListe(r
				.getRechnungID());
		if (rechnungszeilen.isEmpty()) {
			PdfPCell c = new PdfPCell(new Phrase(
					"Keine Rechnungszeilen vorhanden"));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			if (r instanceof Ausgangsrechnung) {
				c.setColspan(6);
			} else {
				c.setColspan(5);
			}
			table.addCell(c);
			return table;
		}
		double summe = 0;
		double summeOhne = 0;
		for (Rechnungszeile rz : rechnungszeilen) {
			table.addCell(String.valueOf(rz.getRechnungszeileID()));
			table.addCell(String.valueOf(rz.getKommentar()));
			if (r instanceof Ausgangsrechnung) {
				table.addCell(String.valueOf(rz.getAngebotID()));
			}
			table.addCell(String.valueOf(rz.getSteuersatz()));
			table.addCell(String.valueOf(rz.getBetrag()));
			double betrag = rz.getBetrag();
			double steuersatz = rz.getSteuersatz();
			double betragOhne = betrag - (betrag / 100 * steuersatz);
			table.addCell(String.valueOf(betragOhne));
			summe += rz.getBetrag();
			summeOhne += betragOhne;
		}

		PdfPCell c = new PdfPCell(new Phrase("Summe"));
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		if (r instanceof Ausgangsrechnung) {
			c.setColspan(4);
		} else {
			c.setColspan(3);
		}
		table.addCell(c);
		table.addCell(String.valueOf(summe));
		table.addCell(String.valueOf(summeOhne));

		return table;

	}

	@SuppressWarnings("unused")
	private void addTitlePage(Document document) throws DocumentException {
		Paragraph preface = new Paragraph();
		// We add one empty line
		addEmptyLine(preface, 1);
		// Lets write a big header
		preface.add(new Paragraph("Title of the document", catFont));

		addEmptyLine(preface, 1);
		// Will create: Report generated by: _name, _date
		preface.add(new Paragraph(
				"Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				smallBold));
		addEmptyLine(preface, 3);
		preface.add(new Paragraph(
				"This document describes something which is very important ",
				smallBold));

		addEmptyLine(preface, 8);

		preface.add(new Paragraph(
				"This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
				redFont));

		document.add(preface);
		// Start a new page
		document.newPage();
	}

	@SuppressWarnings("unused")
	private void addContent(Document document) throws DocumentException {
		Anchor anchor = new Anchor("First Chapter", catFont);
		anchor.setName("First Chapter");

		// Second parameter is the number of the chapter
		Chapter catPart = new Chapter(new Paragraph(anchor), 1);

		Paragraph subPara = new Paragraph("Subcategory 1", subFont);
		Section subCatPart = catPart.addSection(subPara);
		subCatPart.add(new Paragraph("Hello"));

		subPara = new Paragraph("Subcategory 2", subFont);
		subCatPart = catPart.addSection(subPara);
		subCatPart.add(new Paragraph("Paragraph 1"));
		subCatPart.add(new Paragraph("Paragraph 2"));
		subCatPart.add(new Paragraph("Paragraph 3"));

		// Add a list
		createList(subCatPart);
		Paragraph paragraph = new Paragraph();
		addEmptyLine(paragraph, 5);
		subCatPart.add(paragraph);

		// Add a table
		createTable(subCatPart);

		// Now add all this to the document
		document.add(catPart);

		// Next section
		anchor = new Anchor("Second Chapter", catFont);
		anchor.setName("Second Chapter");

		// Second parameter is the number of the chapter
		catPart = new Chapter(new Paragraph(anchor), 1);

		subPara = new Paragraph("Subcategory", subFont);
		subCatPart = catPart.addSection(subPara);
		subCatPart.add(new Paragraph("This is a very important message"));

		// Now add all this to the document
		document.add(catPart);

	}

	private void createTable(Section subCatPart) throws BadElementException {
		PdfPTable table = new PdfPTable(3);

		// t.setBorderColor(BaseColor.GRAY);
		// t.setPadding(4);
		// t.setSpacing(4);
		// t.setBorderWidth(1);

		PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Table Header 2"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase("Table Header 3"));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(c1);
		table.setHeaderRows(1);

		table.addCell("1.0");
		table.addCell("1.1");
		table.addCell("1.2");
		table.addCell("2.1");
		table.addCell("2.2");
		table.addCell("2.3");

		subCatPart.add(table);

	}

	private void createList(Section subCatPart) {
		List list = new List(true, false, 10);
		list.add(new ListItem("First point"));
		list.add(new ListItem("Second point"));
		list.add(new ListItem("Third point"));
		subCatPart.add(list);
	}

	private void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}
}