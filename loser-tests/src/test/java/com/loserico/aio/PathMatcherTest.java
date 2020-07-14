package com.loserico.aio;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * We commonly take advantage of pattern matching to filter out those entries of
 * interest when obtaining a directory listing. For example, we might specify ls -l
 * *.html (Unix/Linux) or dir *.html to obtain a list of those files whose extension
 * is .html.
 * 
 * NIO.2 provides java.nio.file.PathMatcher to support the pattern matching of paths.
 * 
 * FileSystem’s PathMatcher getPathMatcher(String syntaxAndPattern) method returns a
 * PathMatcher object for matching paths against the pattern described by
 * syntaxAndPattern, which identifies a pattern language (syntax) and a pattern
 * (pattern) via this syntax:
 * 
 * syntax:pattern
 * 
 * Two pattern languages are supported: regex and glob. When you specify regex for
 * syntax, you can specify any regular expression for pattern. For example, you might
 * specify regex:([^\s]+(\.(?i)(png|jpg))$) to match all files with .png and .jpg
 * extensions.
 * 
 * Alternatively, you can specify glob for syntax. The glob pattern language is more
 * limited than regex; it resembles regular expressions with a simpler syntax.
 * 
 * @of
 * - The * character matches zero or more characters of a name element without crossing directory boundaries.
 * - The ** characters match zero or more characters crossing directory boundaries.
 * - The ? character matches exactly one character of a name element.
 * - The backslash character (\) is used to escape characters that would otherwise be interpreted as special characters. 
 *   For example, the expression \\ matches a single backslash and the expression \{ matches a left brace.
 * - The [ and ] characters delimit a bracket expression that matches a single character of a name element out of a set of characters. 
 *   For example, [abc] matches a, b, or c. 
 *   The hyphen (-) may be used to specify a range, so [a-z] specifies a range that matches from a to z (inclusive). 
 *   These forms can be mixed, so [abce-g] matches a, b, c, e, f, or g. 
 *   If the character after the [ is a !, the ! is used for negation, so [!a-c] matches any character except for a, b, or c.
 * - Within a bracket expression, the *, ?, and \ characters match themselves. 
 *   The hyphen matches itself when it’s the first character in the brackets, or when it’s the first character after the ! when negating.
 * - The { and } characters identify a group of subpatterns, where the group matches when any subpattern in the group matches. 
 *   The comma is used to separate the subpatterns. Groups cannot be nested.
 * - Leading period/dot characters in file names are treated as regular characters in match operations. 
 *   For example, the * glob pattern matches file name .login.
 * - All other characters match themselves in an implementation-dependent manner. 
 *   This includes characters representing any name separators.
 * - The matching of root elements is highly implementationdependent and is not specified.  
 * @on
 * @author Rico Yu
 * @since 2016-12-17 09:19
 * @version 1.0
 *
 */
public class PathMatcherTest {

	@Test
	public void testPathMatcher() {
		List<String[]> targets = new ArrayList<>();
		targets.add(new String[] { "glob:*.java", "PathMatcherTest.java" });
		targets.add(new String[] { "glob:*.java", "PathMatcherDemo.txt" });
		targets.add(new String[] { "regex:([^\\s]+(\\.(?i)(png|jpg))$)", "figure1.jpg" });
		FileSystem fsDefault = FileSystems.getDefault();
		for (String[] targetArray : targets) {
			String pattern = targetArray[0];
			String target = targetArray[1];
			PathMatcher pm = fsDefault.getPathMatcher(pattern);
			if (pm.matches(fsDefault.getPath(target))) {
				System.out.printf("%s matches pattern%n", target);
			} else {
				System.out.printf("%s doesn't match pattern%n", target);
			}
		}
	}
}
