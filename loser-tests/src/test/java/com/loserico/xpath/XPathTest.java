package com.loserico.xpath;

import com.loserico.common.lang.utils.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 通过XPath 解析 XML/html 
 * 
 * https://jsoup.org
 * <p>
 * Copyright: Copyright (c) 2019-04-23 21:54
 * <p>
 * Company: Sexy Uncle Inc.
 * <p>
 * @author Rico Yu  ricoyu520@gmail.com
 * @version 1.0
 * @on
 */
public class XPathTest {

	static String sample1 = IOUtils.readClassPathFileAsString("sample1.html");
	static String sample2 = IOUtils.readClassPathFileAsString("sample2.xml");

	@Test
	public void testParseDoc() {
		String html = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);
		System.out.println(doc);
		Document sample1Doc = Jsoup.parse(sample1);
		System.out.println(sample1Doc);
		Document sample2Doc = Jsoup.parse(sample2);
		System.out.println(sample2Doc);
	}

	/**
	 * The parseBodyFragment method creates an empty shell document, and inserts the parsed HTML
	 * into the body element. If you used the normal Jsoup.parse(String html) method, you would
	 * generally get the same result, but explicitly treating the input as a body fragment ensures
	 * that any bozo HTML provided by the user is parsed into the body element.
	 * 
	 * The Document.body() method retrieves the element children of the document's body element; it
	 * is equivalent to doc.getElementsByTag("body").
	 */
	@Test
	public void testParseBodyFragment() {
		String html = "<div><p>Lorem ipsum.</p>";
		Document doc = Jsoup.parseBodyFragment(html);
		Element body = doc.body();
		System.out.println(body);
	}

	/**
	 * The connect(String url) method creates a new Connection, and get() fetches and parses a HTML
	 * file. If an error occurs whilst fetching the URL, it will throw an IOException, which you
	 * should handle appropriately.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadFromUrl() throws IOException {
		Document doc = Jsoup.connect("http://example.com/").get();
		String title = doc.title();
		System.out.println(title);
	}

	/**
	 * The Connection interface is designed for method chaining to build specific requests
	 * 
	 * This method only suports web URLs (http and https protocols); if you need to load from a
	 * file, use the parse(File in, String charsetName) method instead.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadFromUrlSpecific() throws IOException {
		Document doc = Jsoup.connect("http://example.com")
				.data("query", "Java")
				.userAgent("Mozilla")
				.cookie("auth", "token")
				.timeout(3000)
				.post();
		System.out.println(doc);
	}

	@Test
	public void testSelect() {
		String html = "<html><div><a href='https://github.com'>github.com</a></div>" +
				"<table><tr><td>a</td><td>b</td></tr></table></html>";
		Document document = Jsoup.parse(html);
		String result = Xsoup.compile("//a/@href").evaluate(document).get();
		System.out.println(result);
		assertEquals("https://github.com", result);

		List<String> list = Xsoup.compile("//tr/td/text()").evaluate(document).list();
		System.out.println(list);
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
	}

	@Test
	public void testNodeName() {
		Document document = Jsoup.parse(sample1);
		List<String> bookNodes = Xsoup.compile("book").evaluate(document).list();
		System.out.println(bookNodes);
	}

	@Test
	public void testSelectRoot() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("/html").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements.first().tagName());
	}
	
	@Test
	public void testChildOfBookstore() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("bookstore/book").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements.first().tagName());
		System.out.println(elements);
	}
	
	@Test
	public void testAllBookElements() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("//book").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements.first().tagName());
		System.out.println(elements);
	}

	@Test
	public void testSelectLangAttribute() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("//book/@lang").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements);
	}
	
	@Test
	public void testSelectHref() {
		Document document = Jsoup.parse(sample1);
		System.out.println(Xsoup.compile("//@href").evaluate(document).get());
		String s = Xsoup.compile("//a/@href").evaluate(document).get();
		System.out.println(s);
	}
	
	@Test
	public void testSelectFirstChild() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("bookstore/book[1]").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements);
	}
	
	@Test
	public void testSelectLastChild() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("bookstore/book[last()]").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements);
	}
	
	@Test
	public void testSelectPosition() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("bookstore/book[position() < 3]").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements);
	}
	
	@Test
	public void testSelectByPrice() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("bookstore/book[price > 30]").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements);
	}
	
	@Test
	public void testSelectMulti() {
		Document document = Jsoup.parse(sample1);
		Elements elements = Xsoup.compile("//book/title|//book/price").evaluate(document).getElements();
		System.out.println(elements.size());
		System.out.println(elements);
	}
}
