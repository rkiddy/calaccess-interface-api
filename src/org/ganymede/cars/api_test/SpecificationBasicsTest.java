package org.ganymede.cars.api_test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SpecificationBasicsTest {

	@BeforeClass
	public static void setup() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;

		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(new File(U.UPLOAD_DATA_SPEC));
		} catch (javax.xml.parsers.ParserConfigurationException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		} catch (org.xml.sax.SAXException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		} catch (java.io.IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	private static Document document;

	@Test
	public void testSchemaDocumentLoadable() {
		Assert.assertNotNull(document);
	}

	@SuppressWarnings("serial")
	List<String> fppcForms = new ArrayList<String>() {
		{
			add("Form400");
			add("Form401");
			add("Form402");
			add("Form410");
			add("Form425");
			add("Form450");
			add("Form460");
			add("Form461");
			add("Form470Short");
			add("Form470Supplement");
			add("Form496");
			add("Form498");
			add("Form511");
			add("Form601");
			add("Form601");
			add("Form602");
			add("Form603");
			add("Form604");
			add("Form605");
			add("Form606");
			add("Form607");
			add("Form615");
			add("Form625");
			add("Form630");
			add("Form635");
			add("Form640");
			add("Form645");
			add("Form690");
		}
	};

	@Test
	public void testFormsAreSpecified() {

		List<String> found = new ArrayList<String>();

		Element root = document.getDocumentElement();

		NodeList children = root.getChildNodes();

		for (int idx = 0; idx < children.getLength(); idx++) {
			Node node = children.item(idx);
			if (node.getAttributes() != null && node.getAttributes().getNamedItem("name") != null) {
				found.add(node.getAttributes().getNamedItem("name").getNodeValue());
			}
		}

		for (String target : fppcForms) {
			Assert.assertTrue("Could not find element for \"" + target + "\"", found.contains(target));
		}
	}
}
