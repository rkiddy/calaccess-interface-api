package org.ganymede.cars;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

public class U {

	public final static String DEFAULT_CAA_URL_BASE = "https://cal-access-int.us-w1.cloudhub.io/api";
	public final static String DEFAULT_CADA_URL_BASE = "https://cal-access-data-int.us-w1.cloudhub.io/api";

	public static final String FORM410_ORIGINAL_MOCK = "data/form410_original_mock.xml";
	public static final String FORM410_FIXED_MOCK = "data/form410_fixed_mock.xml";
	public static final String UPLOAD_DATA_SPEC = "schema/CAL-ACCESS.xsd";

	public static final int EXPECT_PASS = 0;
	public static final int EXPECT_FAIL = 1;
	public static final int EXPECT_EMPTY = 2;
	public static final int EXPECT_ANYTHING = 3;
	public static final int EXPECT_NOTHING = 4;

	public static String clientId = null;
	public static String clientSecret = null;
	public static String vendorCode = null;
	public static String vendorEmail = null;

	public static boolean verbose = false;

	static {

		Properties p = U.readPropertiesFile(System.getProperty("user.home") + File.separator + ".calaccess_api.txt");

		clientId = p.getProperty("client_id");
		clientSecret = p.getProperty("client_secret");
		vendorCode = p.getProperty("vendorCode");
		vendorEmail = p.getProperty("vendorEmail");

		verbose = p.getProperty("verbose", "false").equals("true");
	}

	public static Properties readPropertiesFile(String fileName) {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(fileName);
			prop = new Properties();
			prop.load(fis);
		} catch (java.io.FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (java.io.IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	public static Object json(String str) {

		JSONParser parser = new JSONParser();

		Object topLevel = null;

		try {
			topLevel = parser.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return topLevel;
	}

	public static String getCall(String urlStr, int expected) {

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
			Assert.assertEquals(200, status);
		}
		if (expected == EXPECT_FAIL) {
			Assert.assertEquals(500, status);
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
			if ((expected == EXPECT_PASS || expected == EXPECT_ANYTHING) && verbose) {
				e.printStackTrace();
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
			if ((expected == EXPECT_PASS || expected == EXPECT_ANYTHING) && verbose) {
				e.printStackTrace();
			}
		}

		if (verbose) {
			System.out.println("url:\n" + urlStr);
			System.out.println("result:\n" + result);
		}

		return result.toString();
	}
}
