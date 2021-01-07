package org.ganymede.cars;

import java.io.FileInputStream;
import java.util.Properties;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class U {

	public final static String DEFAULT_CAA_URL_BASE = "https://cal-access-int.us-w1.cloudhub.io/api";
	public final static String DEFAULT_CADA_URL_BASE = "https://cal-access-data-int.us-w1.cloudhub.io/api";

	public static final String FORM410_ORIGINAL_MOCK = "data/form410_original_mock.xml";
	public static final String FORM410_FIXED_MOCK = "data/form410_fixed_mock.xml";
	public static final String UPLOAD_DATA_SPEC = "schema/CAL-ACCESS.xsd";

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
}
