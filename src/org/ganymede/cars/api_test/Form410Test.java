package org.ganymede.cars.api_test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

public class Form410Test {

	private final static String url_base = "https://anypoint.mulesoft.com/mocking/api/v1/links/4da08368-987e-4dee-af7d-de38caf50e61";

	@Test
	/**
	 * If this fails, it may mean that the mocking service in turned off and integration testing should be started.
	 */
	public void testMockedFirstForm410() {

		try {
			String filerID = "1234567";

			URL url = new URL(url_base + "/filers/" + filerID + "/forms?form_type=F410");

			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("Content-Type", "application/xml");
			con.setRequestProperty("vendorEmail", "jsmith@example.com");
			con.setRequestProperty("client_id", "6779ef20e75817b79602");
			con.setRequestProperty("client_secret", "e5868ebb45fc2ad9f96c");
			con.setRequestProperty("vendorCode", "Statecraft");

			con.setDoOutput(true);

			DataOutputStream out = new DataOutputStream(con.getOutputStream());

			Scanner input = new Scanner(new File(U.FORM410_ORIGINAL_MOCK));
			while (input.hasNextLine()) {
				String data = input.nextLine();
				out.writeBytes(data);
			}
			input.close();

			out.flush();
			out.close();

			int status = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			Assert.assertEquals(200, status);

			Object obj = new JSONParser().parse(content.toString()); 

	        JSONObject response = (JSONObject) obj;

	        Assert.assertEquals("2020-09-07", response.get("createdDate"));
	        Assert.assertEquals("Received", response.get("status"));
	        Assert.assertEquals("CA-0000001", response.get("submissionId"));

		} catch (java.io.IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}
}
