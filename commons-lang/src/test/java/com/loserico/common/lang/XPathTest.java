package com.loserico.common.lang;

import com.loserico.common.lang.utils.IOUtils;
import lombok.SneakyThrows;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;

/**
 * https://www.baeldung.com/java-xpath
 * <p>
 * Copyright: (C), 2021-01-10 15:47
 * <p>
 * <p>
 * Company: Information & Data Security Solutions Co., Ltd.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
public class XPathTest {
	
	@SneakyThrows
	@Test
	public void test1() {
		FileInputStream fileInputStream = new FileInputStream(IOUtils.readClasspathFileAsFile("XPathTutorials.xml"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = factory.newDocumentBuilder().parse(fileInputStream);
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		//We can retrieve the tutorial list contained in the root node by using the expression
		String expression = "/Tutorials/Tutorial";
		NodeList nodeList = (NodeList) xPath.compile(expression)
				.evaluate(document, XPathConstants.NODESET);
	}
}
