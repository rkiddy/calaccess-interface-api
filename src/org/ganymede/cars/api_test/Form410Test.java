package org.ganymede.cars.api_test;

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

import org.junit.Assert;
import org.junit.Test;

public class Form410Test {

	private final static String url_base = "https://cal-access-int.us-w1.cloudhub.io/api";

	//@Test
	/**
	 * This test uses the request supplied in the documentation, which fails.
	 *
	 * S ee
	 */
	public void testMockedFirstForm410() {

		String filerID = "0001118";

		URL url = null;
		try {
			url = new URL(url_base + "/filers/" + filerID + "/forms?form_type=F410");
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

			Scanner input = new Scanner(new File(U.FORM410_ORIGINAL_MOCK));
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
			try (InputStream fis = con.getErrorStream();
					InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
					BufferedReader br = new BufferedReader(isr)) {
				br.lines().forEach(line -> System.out.println(line));
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
	}

	@Test
	public void testMockedFirstFixedForm410() {

		String filerID = "0001118";

		URL url = null;
		try {
			url = new URL(url_base + "/filers/" + filerID + "/forms?form_type=F410");
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

			Assert.assertEquals(
					"{  \"message\": \"Vendor is not authorized to access this form\"}",
					error.toString());

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
