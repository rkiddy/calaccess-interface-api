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
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Form410Test {

	private static String urlBase = null;

	private static final int DATA_IS_STRING = 0;
	private static final int DATA_IS_FILE = 1;

	@BeforeClass
	public static void setup() {

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		urlBase = p.getProperty("caa_url_base", U.DEFAULT_CAA_URL_BASE);
	}


	String data =
			"<?xml version='1.0' encoding='UTF-8'?>\n"
			+ "<Form410 xmlns=\"http://cal-access.sos.ca.gov/\">\n"
			+ "<Header>\n"
			+ "<CARS_Ver/>\n"
			+ "<CARS_Form>410</CARS_Form>\n"
			+ "<Soft_Name>Nefile</Soft_Name>\n"
			+ "<Soft_Ver>0.01</Soft_Ver>\n"
			+ "<FilerId>1450006</FilerId>\n"
			+ "</Header>\n"
			+ "<Section0>\n"
			+ "<TypeOfStatement>Amendment</TypeOfStatement>\n"
			+ "<NotYetQualified>true</NotYetQualified>\n"
			+ "<DateofTerminationOrQualification>2021-01-01</DateofTerminationOrQualification>"
			+ "</Section0>\n"
			+ "<Section1>\n"
			+ "<IDNumber>1450006</IDNumber>\n"
			+ "<NameOfCommittee>Godwanaland Enterprises Inc</NameOfCommittee>\n"
			+ "<AddressOfCommittee.Country>US</AddressOfCommittee.Country>\n"
			+ "<AddressOfCommittee.AddressLine1>1000 Laurie Ave</AddressOfCommittee.AddressLine1>\n"
			+ "<AddressOfCommittee.City>San Jose</AddressOfCommittee.City>\n"
			+ "<AddressOfCommittee.State>CA</AddressOfCommittee.State>\n"
			+ "<AddressOfCommittee.ZipCode>95125</AddressOfCommittee.ZipCode>\n"
			+ "<PhoneNumber>408-242-5813</PhoneNumber>\n"
			+ "<MailingAddressOfCommittee.Country>USA</MailingAddressOfCommittee.Country>\n"
			+ "<MailingAddressOfCommittee.AddressLine1>1000 Laurie Ave</MailingAddressOfCommittee.AddressLine1>\n"
			+ "<MailingAddressOfCommittee.City>San Jose</MailingAddressOfCommittee.City>\n"
			+ "<MailingAddressOfCommittee.State>CA</MailingAddressOfCommittee.State>\n"
			+ "<MailingAddressOfCommittee.ZipCode>95125</MailingAddressOfCommittee.ZipCode>\n"
			+ "<EmailAddress>ray@godwanaland.com</EmailAddress>\n"
			+ "<Fax/>\n"
			+ "<County>Santa Clara</County>\n"
			+ "<Jurisdiction>California</Jurisdiction>\n"
			+ "</Section1>\n"
			+ "<Section2>\n"
		    + "<TransactionID>V-001</TransactionID>\n"
		    + "<CARS_Id>CN000010</CARS_Id>\n"
	        + "<Name.FirstName>Ray</Name.FirstName>\n"
	        + "<Name.LastName>Kiddy</Name.LastName>\n"
	        + "<Address.Country>US</Address.Country>\n"
	        + "<Address.AddressLine1>1000 Laurie Ave</Address.AddressLine1>\n"
	        + "<Address.City>San Jose</Address.City>\n"
	        + "<Address.State>CA</Address.State>\n"
	        + "<Address.ZipCode>95125</Address.ZipCode>\n"
	        + "<PhoneNumber>408-242-5813</PhoneNumber>\n"
	        + "<EmailAddress>ray@ganymede.org</EmailAddress>\n"
	        + "<Role>Treasurer</Role>\n"
			+ "</Section2>\n"
			+ "<Section3>\n"
	        + "<TransactionID>V-002</TransactionID>\n"
	        + "<CARS_Id>CN000011</CARS_Id>\n"
	        + "<ExecutedOn>2020-11-19</ExecutedOn>\n"
	        + "<NameOfResponsibleOfficer.FirstName>Ray</NameOfResponsibleOfficer.FirstName>\n"
	        + "<NameOfResponsibleOfficer.MiddleName/>\n"
	        + "<NameOfResponsibleOfficer.LastName>Kiddy</NameOfResponsibleOfficer.LastName>\n"
			+ "</Section3>\n"
			+ "<FinancialInstitution />\n"
			+ "<Section4-ControlledCommittee />\n"
			+ "<Section4-PrimarilyFormedCommittee/>\n"
			+ "<Section4-GeneralPurposeCommittee />\n"
			+ "<Section4-SponsoredCommittee/>\n"
			+ "<Section4-SmallContributorCommittee />\n"
			+ "</Form410>";


	@Test
	public void testForm410WithData00() {

		String result = postCall( urlBase + "/filers/1450006/forms?form_type=F410", DATA_IS_STRING, data, U.EXPECT_FAIL);

		JSONObject top = (JSONObject) U.json(result);
		Assert.assertNotNull(top.get("message"));
	}

	public String postCall(String urlStr, int postDataType, String postData, int expected) {

		URL url = null;
		try {
			url = new URL(urlStr);
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

		if (U.clientId != null) con.setRequestProperty("client_id", U.clientId);
		if (U.clientSecret != null) con.setRequestProperty("client_secret", U.clientSecret);
		if (U.vendorCode != null) con.setRequestProperty("vendorCode", U.vendorCode);
		if (U.vendorEmail != null) con.setRequestProperty("vendorEmail", U.vendorEmail);

		con.setDoOutput(true);

		try {
			DataOutputStream out = new DataOutputStream(con.getOutputStream());

			if (postDataType == DATA_IS_FILE) {

				Scanner input = new Scanner(new File(postData));
				while (input.hasNextLine()) {
					String data = input.nextLine();
					out.writeBytes(data);
				}
				input.close();
			}

			if (postDataType == DATA_IS_STRING) {
				out.writeBytes(postData);
			}

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

		if (expected == U.EXPECT_FAIL) {
			Assert.assertEquals(500, status);
		}

		if (expected == U.EXPECT_PASS) {
			Assert.assertEquals(200, status);
		}

		StringBuilder result = new StringBuilder();

		try {

			InputStream fis = con.getErrorStream();

			if (fis != null) {
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
					BufferedReader br = new BufferedReader(isr)) {
					br.lines().forEach(line -> result.append(line + "\n"));
				}
			}

		} catch (java.io.IOException e) {
			System.err.println(e.getStackTrace()[1]);
		}

		if (expected == U.EXPECT_FAIL) {

			if (U.verbose) {
				System.out.println("url:\n" + urlStr);
				System.out.println("result:\n" + result);
			}

			return result.toString();
		}

		try {

			InputStream fis = con.getInputStream();

			if (fis != null) {
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(isr)) {
					br.lines().forEach(line -> result.append(line + "\n"));
				}
			}

		} catch (java.io.IOException e) {
			System.err.println(e.getStackTrace()[1]);
		}

		if (U.verbose) {
			System.out.println("url:\n" + urlStr);
			System.out.println("result:\n" + result);
		}

		return result.toString();
	}
}
