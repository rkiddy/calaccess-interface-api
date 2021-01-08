 package org.ganymede.cars.cal_access_filing;

import java.io.File;
import java.util.Properties;

import org.ganymede.cars.U;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unused")
public class Form400Test {

	private static String urlBase = null;

	private static String clientId = null;
	private static String clientSecret = null;
	private static String vendorCode = null;
	private static String vendorEmail = null;

	private static boolean verbose = false;

	@BeforeClass
	public static void setup() {

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		urlBase = p.getProperty("caa_url_base", U.DEFAULT_CAA_URL_BASE);

		clientId = p.getProperty("client_id");
		clientSecret = p.getProperty("client_secret");
		vendorCode = p.getProperty("vendorCode");
		vendorEmail = p.getProperty("vendorEmail");

		verbose = p.getProperty("verbose", "false").equals("true");
	}

	@Test
	public void testNothing() {}

}
