package org.ganymede.cars.cal_access_api;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.ganymede.cars.U;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class FilersTest {

	private static final String FILER_FOR_MEASURE_NAME = "Approve this Ballot Measure";
	private static final String FILER_FOR_MEASURE_ID = "1378045";

	private static final String FILER_FOR_COMMITTEE_NAME = "AM Test Controlled Committee 6";
	private static final String FILER_FOR_COMMITTEE_ID = "1378376";

	private static final String FILER_FOR_LOBBYIST_NAME = "Smith C";
	private static final String FILER_FOR_LOBBYIST_ID = "1378054";

	private static final String FILER_FOR_CANDIDATE_NAME = "Ryan Williams";
	private static final String FILER_FOR_CANDIDATE_ID = "1378013";

	private static final String FILER_FOR_CANDIDATE_ID_ALT = "1378261";

	@Test
	public void testGetFilersForMeasures() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers?type=measure&from=2019&to=2020",
				200,
				false);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject)obj;
			if (FILER_FOR_MEASURE_NAME.equals(jObj.get("name")) && FILER_FOR_MEASURE_ID.equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilersForCommittees() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers?type=committee&from=2019&to=2020",
				200,
				false);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject)obj;
			if (FILER_FOR_COMMITTEE_NAME.equals(jObj.get("name")) && FILER_FOR_COMMITTEE_ID.equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilersForLobbyists() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers?type=lobbyist&from=2019&to=2020",
				200,
				false);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject)obj;
			if (FILER_FOR_LOBBYIST_NAME.equals(jObj.get("name")) && FILER_FOR_LOBBYIST_ID.equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilersForCandidates() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers?type=candidate&from=2019&to=2020",
				200,
				false);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject)obj;
			if (FILER_FOR_CANDIDATE_NAME.equals(jObj.get("name")) && FILER_FOR_CANDIDATE_ID.equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilerProfile() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers/" + FILER_FOR_MEASURE_ID,
				200,
				false);

		JSONArray top = (JSONArray) U.json(result);
		JSONObject obj1 = (JSONObject)top.get(0);
		JSONObject obj2 = (JSONObject)obj1.get("billingAddress");
		Assert.assertEquals("444 Ballot St.", obj2.get("street"));

		result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers/" + FILER_FOR_COMMITTEE_ID,
				200,
				false);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject)top.get(0);
		Assert.assertEquals("AM Test Controlled Committee 6", obj1.get("name"));

		result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers/" + FILER_FOR_LOBBYIST_ID,
				200,
				false);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject)top.get(0);
		obj2 = (JSONObject)obj1.get("contactInformation");
		Assert.assertEquals("idasmith@yopmail.com", obj2.get("email"));

		result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers/" + FILER_FOR_CANDIDATE_ID,
				200,
				false);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject)top.get(0);
		Assert.assertEquals("Ryan Williams", obj1.get("name"));
	}

	@Test
	public void testGetFilerTopTenContributors() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers/" + FILER_FOR_CANDIDATE_ID_ALT + "/top10Contributors?from=2019&to=2020",
				200,
				false);

		JSONObject top = (JSONObject) U.json(result);
		JSONArray array1 = (JSONArray)top.get("top10Contributors");
		JSONObject obj1 = (JSONObject)array1.get(0);
		Assert.assertEquals("Central Board of Higher Education", obj1.get("committeeName"));
	}

	//@Test
	public void testGetFilerTopTenContributorsEmpty() {

		String result = getFilersCall(
				"https://cal-access-data-int.us-w1.cloudhub.io/api/filers/" + FILER_FOR_CANDIDATE_ID + "/top10Contributors?from=2019&to=2020",
				200,
				false);

		System.out.println("result: " + result);
	}

	public String getFilersCall(String urlStr, int expectedStatus, boolean returnErrorOutput) {

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
			con.setRequestMethod("GET");
		} catch (java.net.ProtocolException e) {
			e.printStackTrace();
		}

		con.setRequestProperty("Content-Type", "application/json");

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		con.setRequestProperty("client_id", p.getProperty("client_id"));
		con.setRequestProperty("client_secret", p.getProperty("client_secret"));
		con.setRequestProperty("vendorCode", p.getProperty("vendorCode"));

		int status = -1;
		try {
			status = con.getResponseCode();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

		if ( ! returnErrorOutput) {
			Assert.assertEquals(expectedStatus, status);
		}

		StringBuilder result = new StringBuilder();

		try {

			InputStream fis = con.getErrorStream();

			if (fis != null) {
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(isr)) {
					if (returnErrorOutput) {
						br.lines().forEach(line -> result.append(line + "\n"));
					}
				}
			}

		} catch (java.io.IOException e) {
			;
			e.printStackTrace();
		}

		try {

			InputStream fis = con.getInputStream();

			if (fis != null) {
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(isr)) {
					if ( ! returnErrorOutput) {
						br.lines().forEach(line -> result.append(line + "\n"));
					}
				}
			}

		} catch (java.io.IOException e) {
			;
			e.printStackTrace();
		}

		return result.toString();
	}
}
