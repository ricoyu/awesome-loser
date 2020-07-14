package com.loserico.pattern.visitor2;

/**
 * In the Composite Pattern example, we had created an html structure composed of
 * different types of objects. Now suppose that we need to add a css class to the
 * html tags. One way to do this is by adding the class when adding a start tag
 * using the setStartTag method. But this hard coded setting will create
 * inflexibility to our code.
 * 
 * Another way of doing this is by adding a new method like addClass in the parent
 * abstract HtmlTag class. All the child classes will override this method and will
 * provide the css class. One major drawback of this approach is that, if there are
 * many child classes (will be in large html page), it will become very expensive
 * and hectic to implement this method in all the child classes. And suppose, later
 * we need to add another style element in the tags, we again need to do the same
 * thing. The Visitor Design Pattern provides you with a way to add new operations
 * on the objects without changing the classes of the elements, especially when the
 * operations change quite often.
 * 
 * The output after ‘Before Visitor…’ is the same as it results in the Composite
 * Pattern lesson.
 * 
 * Later, we created two concrete visitors and then added them to the concrete html
 * objects using the accept method. The output ‘After visitor…’ shows you the
 * result, in which css class and style elements are added into the html tags.
 * 
 * Please note that the advantage of the Visitor Pattern is that we can add new
 * operations to the objects without changing its classes. For example, we can add
 * some javascript functions like onclick or some angularjs ng tags without
 * modifying the classes.
 * 
 * @author Loser
 * @since Aug 19, 2016
 * @version
 *
 */
public class VisitorPatternTest {

	public static void main(String[] args) {
		System.out.println("Before visitor......... \n");
		HtmlTag htmlTag = new HtmlParentElement("<html>");
		htmlTag.setStartTag("<html>");
		htmlTag.setEndTag("</html>");

		HtmlTag bodyTag = new HtmlParentElement("<body>");
		bodyTag.setStartTag("<body>");
		bodyTag.setEndTag("</body>");
		htmlTag.addChildTag(bodyTag);

		HtmlTag pTag = new HtmlElement("<p>");
		pTag.setStartTag("<p>");
		pTag.setEndTag("</p>");
		pTag.setTagBody("Testing html tag library");
		bodyTag.addChildTag(pTag);

		pTag = new HtmlElement("<p>");
		pTag.setStartTag("<p>");
		pTag.setEndTag("</p>");
		pTag.setTagBody("Paragraph 2");
		bodyTag.addChildTag(pTag);

		htmlTag.generateHtml();
		System.out.println("\nAfter visitor....... \n");

		Visitor cssClass = new CssClassVisitor();
		Visitor style = new StyleVisitor();

		htmlTag = new HtmlParentElement("<html>");
		htmlTag.setStartTag("<html>");
		htmlTag.setEndTag("</html>");
		htmlTag.accept(style);
		htmlTag.accept(cssClass);

		bodyTag = new HtmlParentElement("<body>");
		bodyTag.setStartTag("<body>");
		bodyTag.setEndTag("</body>");
		bodyTag.accept(style);
		bodyTag.accept(cssClass);
		htmlTag.addChildTag(bodyTag);

		pTag = new HtmlElement("<p>");
		pTag.setStartTag("<p>");
		pTag.setEndTag("</p>");
		pTag.setTagBody("Testing html tag library");
		pTag.accept(style);
		pTag.accept(cssClass);
		bodyTag.addChildTag(pTag);

		pTag = new HtmlElement("<p>");
		pTag.setStartTag("<p>");
		pTag.setEndTag("</p>");
		pTag.setTagBody("Paragraph 2");
		pTag.accept(style);
		pTag.accept(cssClass);
		bodyTag.addChildTag(pTag);

		htmlTag.generateHtml();
	}
}