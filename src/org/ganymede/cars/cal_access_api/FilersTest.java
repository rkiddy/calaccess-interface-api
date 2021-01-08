package org.ganymede.cars.cal_access_api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ganymede.cars.U;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FilersTest {

	private static String urlBase = null;

	@BeforeClass
	public static void setup() {

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		urlBase = p.getProperty("cada_url_base", U.DEFAULT_CADA_URL_BASE);
	}

	List<String> filerIds = null;

	@Test
	public void testGetAllFilers() {
		Assert.assertNotEquals(allFilerIds().size(), 0);
	}

	/**
	 * A "helper" test. Do any of the available filers have whatever it is that is being looked for? Run
	 * this test with verbose turned on and find out.
	 */
	//@Test
	public void findSomething() {

		for (String filerId : allFilerIds()) {
			U.getCall(urlBase + "/filers/" + filerId + "/ind-exp-of-committee?from=2019&to=2020", U.EXPECT_ANYTHING);
			//U.getCall(urlBase + "/contributors?firstName=John&lastName=Smith", 0, U.EXPECT_NOTHING);
		}
	}

	@Test
	public void testGetFilersForMeasures() {

		String result = U.getCall(urlBase + "/filers?type=measure&from=2019&to=2020", U.EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("Approve this Ballot Measure".equals(jObj.get("name")) && "1378045".equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilersForCommittees() {

		String result = U.getCall(urlBase + "/filers?type=committee&from=2019&to=2020", U.EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("AM Test Controlled Committee 6".equals(jObj.get("name")) && "1378376".equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilersForLobbyists() {

		String result = U.getCall(urlBase + "/filers?type=lobbyist&from=2019&to=2020", U.EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("Smith C".equals(jObj.get("name")) && "1378054".equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilersForCandidates() {

		String result = U.getCall(urlBase + "/filers?type=candidate&from=2019&to=2020", U.EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);

		boolean found = false;

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("Ryan Williams".equals(jObj.get("name")) && "1378013".equals(jObj.get("id"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	/**
	 * Fetches one of each of the filer types. Verifies a field in the resulting
	 * data.
	 *
	 * TODO This relies on current data and will break if the SoS changes anything.
	 */
	@Test
	public void testGetFilerProfile() {

		String result = U.getCall(urlBase + "/filers/1378045", U.EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);
		JSONObject obj1 = (JSONObject) top.get(0);
		JSONObject obj2 = (JSONObject) obj1.get("billingAddress");
		Assert.assertEquals("444 Ballot St.", obj2.get("street"));

		result = U.getCall(urlBase + "/filers/1378376", U.EXPECT_PASS);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject) top.get(0);
		Assert.assertEquals("AM Test Controlled Committee 6", obj1.get("name"));

		result = U.getCall(urlBase + "/filers/1378054", U.EXPECT_PASS);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject) top.get(0);
		obj2 = (JSONObject) obj1.get("contactInformation");
		Assert.assertEquals("idasmith@yopmail.com", obj2.get("email"));

		result = U.getCall(urlBase + "/filers/1378013", U.EXPECT_PASS);

		top = (JSONArray) U.json(result);
		obj1 = (JSONObject) top.get(0);
		Assert.assertEquals("Ryan Williams", obj1.get("name"));
	}

	@Test
	public void testGetFilerTopTenContributors() {

		String result = U.getCall(
				urlBase + "/filers/1378261/top10Contributors?from=2019&to=2020",
				U.EXPECT_PASS);

		JSONObject top = (JSONObject) U.json(result);
		JSONArray array1 = (JSONArray)top.get("top10Contributors");
		JSONObject obj1 = (JSONObject)array1.get(0);
		Assert.assertEquals("Central Board of Higher Education", obj1.get("committeeName"));
	}

	@Test
	public void testGetFilerTopTenContributorsEmpty() {

		String result = U.getCall(
				urlBase + "/filers/1378013/top10Contributors?from=2019&to=2020",
				U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	// @Test - get "Resource not found". Why?
	public void testGetFilerLateContributorsEmpty() {

		String result = U.getCall(urlBase + "/filers/1378069/lateContributors?from=2018&to=2020",
				U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	@Test
	public void testGetFilerLobbyistEmptoyersEmpty() {

		String result = U.getCall(urlBase + "/filers/1378112/lobbying-employers?from=2018&to=2020",
				U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}


	public void testGetFilerSpendersEmpty() {

		String result = U.getCall(urlBase + "/filers/1378387/spenders?from=2019&to=2020", U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	@Test
	public void testGetFilerContributorPayeesEmpty() {

		String result = U.getCall(urlBase + "/filers/1378314/contributors-payees?from=2018&to=2020", U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	@Test
	public void testGetFilerTargetsEmpty() {

		String result = U.getCall(urlBase + "/filers/1378241/targets?from=2018&to=2020", U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}

	@Test
	public void testGetContributorsEmpty() {

		String result = U.getCall(urlBase + "/contributors?firstName=John&lastName=Smith", U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("unknown description"));
	}

	@Test
	public void testGetFilerCandidatesOfCommittee() {

		String result = U.getCall(urlBase + "/filers/1378081/candidates-of-committee", U.EXPECT_PASS);

		JSONObject top = (JSONObject) U.json(result);
		Assert.assertEquals("Central Board of Higher Education", top.get("committeeName"));
	}

	@Test
	public void testGetFilerIndExps() {

		String result = U.getCall(urlBase + "/filers/1378087/ind-exp-of-committee?from=2019&to=2020", U.EXPECT_PASS);

		boolean found = false;

		JSONArray top = (JSONArray) U.json(result);

		for (Object obj : top.toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("Candidate John F Smith".equals(jObj.get("targetName")) && "1378086".equals(jObj.get("targetId"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	@Test
	public void testGetFilerFirmAndEmployerLobbyist() {

		String result = U.getCall(urlBase + "/filers/1378032/firm-and-employer-of-lobbyist", U.EXPECT_PASS);

		JSONObject top = (JSONObject) U.json(result);

		Assert.assertEquals("1378032", top.get("lobbyistId"));

		boolean found = false;

		
		for (Object obj : ((JSONArray)top.get("lobbyingFirm")).toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("Capital Partners Lobby Firm".equals(jObj.get("name"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);

		found = false;

		for (Object obj : ((JSONArray)top.get("lobbyingEmployer")).toArray()) {
			JSONObject jObj = (JSONObject) obj;
			if ("Vehement Lobbyist Employer".equals(jObj.get("name"))) {
				found = true;
			}
		}

		Assert.assertTrue(found);
	}

	/**
	 *  @see https://github.com/rkiddy/calaccess-interface-api/issues/1
	 */
	//@Test
	public void testGetFilerSpendersWithMuleException() {

		String result = U.getCall(urlBase + "/filers/1378313/spenders", U.EXPECT_EMPTY);

		Assert.assertTrue(result.contains("No records found"));
	}


	public List<String> allFilerIds() {

		if (filerIds == null) {

			filerIds = new ArrayList<>();

			String result = U.getCall(urlBase + "/filers?type=measure&from=2019&to=2020", U.EXPECT_PASS);

			JSONArray top = (JSONArray) U.json(result);
			for (Object obj : top.toArray()) {
				filerIds.add(((JSONObject) obj).get("id").toString());
			}

			result = U.getCall(urlBase + "/filers?type=committee&from=2019&to=2020", U.EXPECT_PASS);

			top = (JSONArray) U.json(result);
			for (Object obj : top.toArray()) {
				filerIds.add(((JSONObject) obj).get("id").toString());
			}

			result = U.getCall(urlBase + "/filers?type=lobbyist&from=2019&to=2020", U.EXPECT_PASS);

			top = (JSONArray) U.json(result);
			for (Object obj : top.toArray()) {
				filerIds.add(((JSONObject) obj).get("id").toString());
			}

			result = U.getCall(urlBase + "/filers?type=candidate&from=2019&to=2020", U.EXPECT_PASS);

			top = (JSONArray) U.json(result);
			for (Object obj : top.toArray()) {
				filerIds.add(((JSONObject) obj).get("id").toString());
			}
		}

		return filerIds;
	}
}
