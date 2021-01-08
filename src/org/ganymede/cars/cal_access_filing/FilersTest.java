package org.ganymede.cars.cal_access_filing;

import java.io.File;
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

		urlBase = p.getProperty("caa_url_base", U.DEFAULT_CAA_URL_BASE);
	}

	@Test
	public void testAccountInfo() {
	
		String result = U.getCall( urlBase + "/account?firstName=John&lastName=Smith", U.EXPECT_PASS);

		JSONArray top = (JSONArray) U.json(result);
		JSONObject obj1 = (JSONObject)top.get(0);
		Assert.assertEquals("1378268", obj1.get("filerId"));
		Assert.assertEquals("John", obj1.get("firstName"));
		Assert.assertEquals("Smith", obj1.get("lastName"));
	}

}
