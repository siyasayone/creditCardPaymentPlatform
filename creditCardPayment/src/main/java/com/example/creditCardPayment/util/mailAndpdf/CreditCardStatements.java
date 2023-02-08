package com.example.creditCardPayment.util.mailAndpdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.example.creditCardPayment.dto.customerTransactions.CustomerTransactionDTO;
import com.example.creditCardPayment.dto.statement.CustomerStatementDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @Generate pdf files for reports
 * @author Siya
 *
 */
public class CreditCardStatements {



	public static void sendStatementToUser(CustomerStatementDTO dto) throws AddressException, MessagingException {
		String smtpHost = "smtp.gmail.com";
		int smtpPort = 587;

		String sender = "mailtestingsample@gmail.com";
		String recipient = dto.getMailId();
		String content = "Statement for ABC Credit Card";
		String subject = "ACCOUNT SUMMARY";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.port", smtpPort);
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("mailtestingsample@gmail.com", "S{iya}@13");
			}

		});

		ByteArrayOutputStream outputStream = null;
		try {
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(content);

			outputStream = new ByteArrayOutputStream();
			writePdf(outputStream, dto);
			byte[] bytes = outputStream.toByteArray();

			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("statement.pdf");

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			InternetAddress iaSender = new InternetAddress(sender);
			InternetAddress iaRecipient = new InternetAddress(recipient);

			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setSender(iaSender);
			mimeMessage.setSubject(subject);
			mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
			mimeMessage.setContent(mimeMultipart);

			Transport.send(mimeMessage);

			System.out.println(
					"sent from " + sender + ", to " + recipient + "; server = " + smtpHost + ", port = " + smtpPort);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != outputStream) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (Exception ex) {
				}
			}
		}

	}
	
	private static Font COURIER = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
	private static Font COURIER_SMALL = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
	private static Font COURIER_SMALL_FOOTER = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

	private static void leaveEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	public static void writePdf(OutputStream outputStream, CustomerStatementDTO dto) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();
		Paragraph paragraph = new Paragraph();
		Paragraph p1 = new Paragraph();
		String localDateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		leaveEmptyLine(paragraph, 1);
		p1.add(new Paragraph("Statement for ABC Credit Card", COURIER));
		p1.setAlignment(Element.ALIGN_CENTER);
		leaveEmptyLine(p1, 1);
		p1.add(new Paragraph("Bill Number " + dto.getBillNumber(), COURIER));
		p1.setAlignment(Element.ALIGN_RIGHT);
		leaveEmptyLine(p1, 1);
		p1.add(new Paragraph("Report generated on " + localDateString, COURIER_SMALL));
		document.add(p1);
		leaveEmptyLine(p1, 3);
		document.add(paragraph);
		PdfPTable table = new PdfPTable(5);
		List<String> columnNames = new ArrayList<String>();
		columnNames.add("Payment Due Date");
		columnNames.add("Total Dues");
		columnNames.add("Minimum Amount Due");
		columnNames.add("Credit Limit");
		columnNames.add("Available Credit Limit");
		for (int i = 0; i < 5; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(columnNames.get(i)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.CYAN);
			table.addCell(cell);
		}

		table.setHeaderRows(1);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 20);
		LocalDate ldate = LocalDate.from(c.toInstant().atZone(ZoneOffset.UTC));
		String s = DateTimeFormatter.ISO_DATE.format(ldate);
		table.addCell(s);
		table.addCell(dto.getTotalDue().toString());
		table.addCell(dto.getMinimumAmountDue().toString());
		table.addCell(dto.getCreditLimit().toString());
		table.addCell(dto.getAvailableCredit().toString());
		document.add(table);
		leaveEmptyLine(paragraph, 3);
		document.add(paragraph);
		Paragraph p2 = new Paragraph();
		p2.add(new Paragraph("Transaction history-Current Month", COURIER_SMALL));
		document.add(p2);
		leaveEmptyLine(p2, 3);
		document.add(paragraph);
		PdfPTable table1 = new PdfPTable(2);
		List<String> columnNames1 = new ArrayList<String>();
		columnNames1.add("Amount");
		columnNames1.add("Transaction Date");
		for (int i = 0; i < 2; i++) {
			PdfPCell cell1 = new PdfPCell(new Phrase(columnNames1.get(i)));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setBackgroundColor(BaseColor.CYAN);
			table1.addCell(cell1);
		}

		table1.setHeaderRows(1);
		table1.setWidthPercentage(100);
		table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		for (CustomerTransactionDTO d : dto.getCustomerTransactionDTO()) {
			if (d.getCustomerId().equals(dto.getCustomerId())) {
				LocalDate ldate1 = LocalDate.from(d.getTransactionDate().toInstant().atZone(ZoneOffset.UTC));
				String s1 = DateTimeFormatter.ISO_DATE.format(ldate1);
				table1.addCell(d.getAmount().toString());
				table1.addCell(s1);
			}

		}
		document.add(table1);
		leaveEmptyLine(paragraph, 3);
		paragraph.setAlignment(Element.ALIGN_MIDDLE);

		document.add(paragraph);
		document.close();
	}

}
