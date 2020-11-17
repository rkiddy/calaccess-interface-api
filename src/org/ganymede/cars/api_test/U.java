package org.ganymede.cars.api_test;

import java.io.FileInputStream;
import java.util.Properties;

public class U {

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
}
