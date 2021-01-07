package org.ganymede.cars.cal_access_filing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.ganymede.cars.U;
import org.junit.Assert;
import org.junit.Test;

public class Form410Test {

	@Test
	public void testOne() { }

	//@Test
	public void testGetForm410() {

		String filerID = "1378088";

		System.out.println("filerID: " + filerID);

		URL url = null;
		try {
			url = new URL(U.URL_BASE + "/filers/" + filerID + "/forms?form_type=F410");
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
		}

		HttpsURLConnection con = null;
		try {
			con = (HttpsURLConnection) url.openConnection();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		try {
			con.setRequestMethod("GET");
		} catch (java.net.ProtocolException e) {
			e.printStackTrace();
		}

		con.setRequestProperty("Content-Type", "application/octet-stream");

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		con.setRequestProperty("vendorEmail", p.getProperty("vendorEmail"));
		con.setRequestProperty("client_id", p.getProperty("client_id"));
		con.setRequestProperty("client_secret", p.getProperty("client_secret"));
		con.setRequestProperty("vendorCode", p.getProperty("vendorCode"));

		int status = -1;
		try {
			status = con.getResponseCode();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		try {

			InputStream fis = con.getErrorStream();

			if (fis != null) {
				System.out.println("error:");
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(isr)) {
					br.lines().forEach(line -> System.out.println(line));
				}
			}

		} catch (java.io.IOException e) {
			;
			e.printStackTrace();
		}

		StringBuffer content = null;

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (java.io.IOException e) {
			/* do nothing yet */ }

		try {
			if (in != null) {
				String inputLine;
				content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();
				con.disconnect(); // System.out.println("content:\n" + content);

				char[] dest = new char[content.length()];
				try {
					content.getChars(0, content.length(), dest, 0);
				} catch (Exception e) {
					System.out.println(e);
				}
				for (int idx = 0, offs = 0; idx < dest.length; idx++, offs++) {
					System.out.print(" ");
					String digit = Integer.toHexString((int) (255 & dest[idx]));
					while (digit.length() < 2) { digit = "0" + digit; }
					System.out.print(digit);
					if (offs > 20) {
						System.out.println("");
						offs = 0;
					}
				}
				System.out.println("\n");
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(200, status);
	}

	@SuppressWarnings("unused")
	private String expectedGetForm410Response = "<?xml version='1.0' encoding='UTF-8'?>\n" + 
			"<Form410 xmlns=\"http://cal-access.sos.ca.gov/\">\n" + 
			"  <Header>\n" + 
			"    <CARS_Ver/>\n" + 
			"    <CARS_Form>410</CARS_Form>\n" + 
			"    <Soft_Name/>\n" + 
			"    <Soft_Ver/>\n" + 
			"    <FilerId>1378088</FilerId>\n" + 
			"  </Header>\n" + 
			"  <Section0>\n" + 
			"    <TypeOfStatement>Amendment</TypeOfStatement>\n" + 
			"    <NotYetQualified>true</NotYetQualified>\n" + 
			"    <DateofTerminationOrQualification>2020-11-16</DateofTerminationOrQualification>\n" + 
			"  </Section0>\n" + 
			"  <Section1>\n" + 
			"    <IDNumber>1378088</IDNumber>\n" + 
			"    <NameOfCommittee>Committee for Electronic Filing</NameOfCommittee>\n" + 
			"    <AddressOfCommittee.AddressLine1>1123 K St. NE</AddressOfCommittee.AddressLine1>\n" + 
			"    <AddressOfCommittee.City>Washington</AddressOfCommittee.City>\n" + 
			"    <AddressOfCommittee.State>DC</AddressOfCommittee.State>\n" + 
			"    <AddressOfCommittee.ZipCode>20002</AddressOfCommittee.ZipCode>\n" + 
			"    <PhoneNumber/>\n" + 
			"    <MailingAddressOfCommittee.Country/>\n" + 
			"    <MailingAddressOfCommittee.AddressLine1/>\n" + 
			"    <MailingAddressOfCommittee.City/>\n" + 
			"    <MailingAddressOfCommittee.State/>\n" + 
			"    <MailingAddressOfCommittee.ZipCode/>\n" + 
			"    <EmailAddress>vincent.c.liang@gmail.com</EmailAddress>\n" + 
			"    <Fax/>\n" + 
			"    <County>Los Angeles</County>\n" + 
			"    <Jurisdiction/>\n" + 
			"  </Section1>\n" + 
			"  <Section2>\n" + 
			"    <TransactionID>A-001</TransactionID>\n" + 
			"    <CARS_Id>CN000013</CARS_Id>\n" + 
			"    <Name.FirstName>Samuel</Name.FirstName>\n" + 
			"    <Name.LastName>Jennings</Name.LastName>\n" + 
			"    <Address.Country/>\n" + 
			"    <Address.AddressLine1>1123 K St. NE</Address.AddressLine1>\n" + 
			"    <Address.City>Washington</Address.City>\n" + 
			"    <Address.State>DC</Address.State>\n" + 
			"    <Address.ZipCode>20002</Address.ZipCode>\n" + 
			"    <PhoneNumber>6023210630</PhoneNumber>\n" + 
			"    <EmailAddress>samuel.jennings2@yopmail.com</EmailAddress>\n" + 
			"    <Role>Treasurer;Principal Officer</Role>\n" + 
			"  </Section2>\n" + 
			"  <Section3>\n" + 
			"    <TransactionID>A-001</TransactionID>\n" + 
			"    <CARS_Id>CN000013</CARS_Id>\n" + 
			"    <ExecutedOn>2020-11-19</ExecutedOn>\n" + 
			"    <NameOfResponsibleOfficer.FirstName>Samuel</NameOfResponsibleOfficer.FirstName>\n" + 
			"    <NameOfResponsibleOfficer.MiddleName/>\n" + 
			"    <NameOfResponsibleOfficer.LastName>Jennings</NameOfResponsibleOfficer.LastName>\n" + 
			"  </Section3>\n" + 
			"  <FinancialInstitution>\n" + 
			"    <NameOfFinancialInstitution/>\n" + 
			"    <BankAccountNumber/>\n" + 
			"    <AddressOfFinancialInstitution.AddressLine1/>\n" + 
			"    <AddressOfFinancialInstitution.City/>\n" + 
			"    <AddressOfFinancialInstitution.State/>\n" + 
			"    <AddressOfFinancialInstitution.ZipCode/>\n" + 
			"    <FinancialInstitution.PhoneNumber/>\n" + 
			"  </FinancialInstitution>\n" + 
			"  <Section4-ControlledCommittee/>\n" + 
			"  <Section4-PrimarilyFormedCommittee/>\n" + 
			"  <Section4-GeneralPurposeCommittee>\n" + 
			"    <Jurisdiction/>\n" + 
			"    <ActivityDescription/>\n" + 
			"  </Section4-GeneralPurposeCommittee>\n" + 
			"  <Section4-SponsoredCommittee/>\n" + 
			"  <Section4-SmallContributorCommittee>\n" + 
			"    <DateQualified/>\n" + 
			"  </Section4-SmallContributorCommittee>\n" + 
			"</Form410>";

	//@Test
	public void testPostForm410() {

		String filerID = "0001118";

		URL url = null;
		try {
			url = new URL(U.URL_BASE + "/filers/" + filerID + "/forms?form_type=F410");
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
		}

		HttpsURLConnection con = null;
		try {
			con = (HttpsURLConnection) url.openConnection();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		try {
			con.setRequestMethod("POST");
		} catch (java.net.ProtocolException e) {
			e.printStackTrace();
		}

		con.setRequestProperty("Content-Type", "application/xml");

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		con.setRequestProperty("vendorEmail", p.getProperty("vendorEmail"));
		con.setRequestProperty("client_id", p.getProperty("client_id"));
		con.setRequestProperty("client_secret", p.getProperty("client_secret"));
		con.setRequestProperty("vendorCode", p.getProperty("vendorCode"));

		con.setDoOutput(true);

		try {
			DataOutputStream out = new DataOutputStream(con.getOutputStream());

			Scanner input = new Scanner(new File(U.FORM410_FIXED_MOCK));
			while (input.hasNextLine()) {
				String data = input.nextLine();
				out.writeBytes(data);
			}
			input.close();

			out.flush();
			out.close();

		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		int status = -1;
		try {
			status = con.getResponseCode();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		try {

			StringBuilder error = new StringBuilder();

			try (InputStream fis = con.getErrorStream();
					InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
					BufferedReader br = new BufferedReader(isr)) {
				br.lines().forEach(line -> error.append(line));
			}

		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		StringBuffer content = null;

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (java.io.IOException e) {
			/* do nothing yet */ }

		try {
			if (in != null) {
				String inputLine;
				content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();
				con.disconnect();
			}
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		Assert.assertEquals(500, status);

//		JSONObject response = null;
//
//		try {
//			if (content != null) {
//				response = (JSONObject) new JSONParser().parse(content.toString());
//			}
//		} catch (org.json.simple.parser.ParseException e) {
//			e.printStackTrace();
//		}
//
//		Assert.assertNotNull(response);
//
//		Assert.assertEquals("2020-09-07", response.get("createdDate"));
//		Assert.assertEquals("Received", response.get("status"));
//		Assert.assertEquals("CA-0000001", response.get("submissionId"));
	}
}
