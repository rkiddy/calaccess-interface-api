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
import org.junit.BeforeClass;
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

	private static final int EXPECT_PASS = 0;
	private static final int EXPECT_FAIL = 1;
	private static final int EXPECT_EMPTY = 2;

	private static String urlBase = null;

	private static String clientId = null;
	private static String clientSecret = null;
	private static String vendorCode = null;

	private static boolean verbose = false;

	@BeforeClass
	public static void setup() {

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		urlBase = p.getProperty("cada_url_base", U.DEFAULT_CADA_URL_BASE);

		clientId = p.getProperty("client_id");
		clientSecret = p.getProperty("client_secret");
		vendorCode = p.getProperty("vendorCode");

		verbose = p.getProperty("verbose", "false").equals("true");
	}

	@Test
	public void testGetFilersForMeasures() {

		String result = getFilersCall(
				urlBase + "/filers?type=measure&from=2019&to=2020",
				200,
				EXPECT_PASS);

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
				urlBase + "/filers?type=committee&from=2019&to=2020",
				200,
				EXPECT_PASS);

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
				urlBase + "/filers?type=lobbyist&from=2019&to=2020",
				200,
				EXPECT_PASS);

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
				urlBase + "/filers?type=candidate&from=2019&to=2020",
				200,
				EXPECT_PASS);

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
				urlBase + "/filers/" + FILER_FOR_MEASURE_ID,
				200,
				EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);
		JSONObject obj1 = (JSONObject)top.get(0);
		JSONObject obj2 = (JSONObject)obj1.get("billingAddress");
		Assert.assertEquals("444 Ballot St.", obj2.get("street"));

		result = getFilersCall(
				urlBase + "/filers/" + FILER_FOR_COMMITTEE_ID,
				200,
				EXPECT_PASS);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject)top.get(0);
		Assert.assertEquals("AM Test Controlled Committee 6", obj1.get("name"));

		result = getFilersCall(
				urlBase + "/filers/" + FILER_FOR_LOBBYIST_ID,
				200,
				EXPECT_PASS);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject)top.get(0);
		obj2 = (JSONObject)obj1.get("contactInformation");
		Assert.assertEquals("idasmith@yopmail.com", obj2.get("email"));

		result = getFilersCall(
				urlBase + "/filers/" + FILER_FOR_CANDIDATE_ID,
				200,
				EXPECT_PASS);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject)top.get(0);
		Assert.assertEquals("Ryan Williams", obj1.get("name"));
	}

	@Test
	public void testGetFilerTopTenContributors() {

		String result = getFilersCall(
				urlBase + "/filers/" + FILER_FOR_CANDIDATE_ID_ALT + "/top10Contributors?from=2019&to=2020",
				200,
				EXPECT_PASS);

		JSONObject top = (JSONObject) U.json(result);
		JSONArray array1 = (JSONArray)top.get("top10Contributors");
		JSONObject obj1 = (JSONObject)array1.get(0);
		Assert.assertEquals("Central Board of Higher Education", obj1.get("committeeName"));
	}

	@Test
	public void testGetFilerTopTenContributorsEmpty() {

		String result = getFilersCall(
				urlBase + "/filers/" + FILER_FOR_CANDIDATE_ID + "/top10Contributors?from=2019&to=2020",
				200,
				EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	@Test
	public void testGetFilerLobbyistEmptoyers() {

		String result = getFilersCall(
				urlBase + "/filers/1378380/lobbying-employers?from=2018&to=2020",
				200,
				EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	@Test
	public void testGetFilerContributorPayees() {

		String result = getFilersCall(
				urlBase + "/filers/1378380/contributors-payees?from=2018&to=2020",
				200,
				EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	//@Test - returns an error. Email sent, because the error looks weird.
	public void testGetFilerSpenders() {

		getFilersCall(
				urlBase + "/filers/1378313/spenders",
				200,
				EXPECT_FAIL);
	}

	public String getFilersCall(String urlStr, int expectedStatus, int expected) {

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

		if (clientId != null) con.setRequestProperty("client_id", clientId);
		if (clientSecret != null) con.setRequestProperty("client_secret", clientSecret);
		if (vendorCode != null) con.setRequestProperty("vendorCode", vendorCode);

		int status = -1;
		try {
			status = con.getResponseCode();
		} catch (java.io.IOException e) {
			System.err.println(e.getStackTrace()[1]);
		}

		if (expected == EXPECT_PASS) {
			Assert.assertEquals(expectedStatus, status);
		}

		StringBuilder result = new StringBuilder();

		try {

			InputStream fis = con.getErrorStream();

			if (fis != null) {
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(isr)) {
					if (expected != EXPECT_PASS) {
						br.lines().forEach(line -> result.append(line + "\n"));
					}
				}
			}

		} catch (java.io.IOException e) {
			if (expected == EXPECT_PASS) {
				System.err.println(e.getStackTrace()[1]);
			}
		}

		try {

			InputStream fis = con.getInputStream();

			if (fis != null) {
				try (InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
						BufferedReader br = new BufferedReader(isr)) {
					if (expected != EXPECT_FAIL) {
						br.lines().forEach(line -> result.append(line + "\n"));
					}
				}
			}

		} catch (java.io.IOException e) {
			if (expected == EXPECT_PASS) {
				System.err.println(e.getStackTrace()[1]);
			}
		}

		if (verbose) {
			System.out.println("url:\n" + urlStr);
			System.out.println("result:\n" + result);
		}

		return result.toString();
	}
}
