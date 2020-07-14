package com.loserico.java8.stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @of
 * Java 8中的新功能特性改变了游戏规则。对Java开发者来说这是一个全新的世界，并且是时候去适应它了。
 * 
 * 在这篇文章里，我们将会去了解传统循环的一些替代方案。
 * 在Java 8的新功能特性中，最棒的特性就是允许我们去表达我们想要完成什么而不是要怎样做。
 * 
 * 这正是循环的不足之处。要确保循环的灵活性是需要付出代价的。return、break 或者 continue都会显著地改变循环的实际表现。
 * 这迫使我们不仅要清楚我们要实现怎样的代码，还要了解循环是怎样工作的。
 * 
 * @on
 * @author Rico Yu	ricoyu520@gmail.com
 * @since 2017-03-18 21:55
 * @version 1.0
 *
 */
public class StreamOverForeachTest {

	/**
	 * 一篇文章拥有一个标题，一个作者和几个标签
	 * @author Rico Yu	ricoyu520@gmail.com
	 * @since 2017-03-18 21:56
	 * @version 1.0
	 *
	 */
	private static class Article {

		private final String title;
		private final String author;
		private final List<String> tags;

		private Article(String title, String author, List<String> tags) {
			this.title = title;
			this.author = author;
			this.tags = tags;
		}

		public String getTitle() {
			return title;
		}

		public String getAuthor() {
			return author;
		}

		public List<String> getTags() {
			return tags;
		}
	}

	/*
	 * 在第一个例子里，我们要在集合中查找包含“Java”标签的第一篇文章。
	 * 看一下使用for循环的解决方案。
	 */
	public Article getFirstJavaArticle(List<Article> articles) {
		for (Article article : articles) {
			if (article.getTags().contains("Java")) {
				return article;
			}
		}
		return null;
	}

	/*
	 * 现在我们使用Stream API的相关操作来解决这个问题
	 * 
	 * 是不是很酷？我们首先使用 filter 操作去找到所有包含Java标签的文章，然后使用 findFirst() 操作去获取第一次出现的文章。
	 * 因为Stream是“延迟计算”（lazy）的并且filter返回一个流对象，所以这个方法仅在找到第一个匹配元素时才会处理元素。
	 */
	public Optional<Article> getFirstJavaArticleByStream(List<Article> articles) {
		return articles.stream()
				.filter(article -> article.getTags().contains("Java"))
				.findFirst();
	}

	/*
	 * 让我们获取所有匹配的元素而不是仅获取第一个。首先使用for循环方案。
	 */
	public List<Article> getAllJavaArticles(List<Article> articles) {
		List<Article> result = new ArrayList<>();
		for (Article article : articles) {
			if (article.getTags().contains("Java")) {
				result.add(article);
			}
		}

		return result;
	}

	/*
	 * 在这个例子里我们使用 collection 操作在返回流上执行少量代码而不是手动声明一个集合并显式地添加匹配的文章到集合里
	 */
	public List<Article> getAllJavaArticlesStream(List<Article> articles) {
		return articles.stream()
				.filter(article -> article.getTags().contains("Java"))
				.collect(Collectors.toList());
	}

	/*
	 * 到目前为止还不错。是时候举一些突出Stream API强大的例子了。
	 * 根据作者来把所有的文章分组。照旧，我们使用循环方案。
	 */
	public Map<String, List<Article>> groupByAuthor(List<Article> articles) {

		Map<String, List<Article>> result = new HashMap<>();

		for (Article article : articles) {
			if (result.containsKey(article.getAuthor())) {
				result.get(article.getAuthor()).add(article);
			} else {
				ArrayList<Article> articles2 = new ArrayList<>();
				articles2.add(article);
				result.put(article.getAuthor(), articles2);
			}
		}

		return result;
	}

	/*
	 * 我们能否找到一个使用流操作的简洁方案来解决这个问题？
	 * 很好！使用 groupingBy 操作和 getAuthor 方法，我们得到了更简洁、可读性更高的代码。
	 */
	public Map<String, List<Article>> groupByAuthorStream(List<Article> articles) {
		return articles.stream()
				.collect(Collectors.groupingBy(Article::getAuthor));
	}

	/*
	 * 现在，我们查找集合中所有不同的标签。
	 */
	public Set<String> getDistinctTags(List<Article> articles) {
		Set<String> result = new HashSet<>();
		for (Article article : articles) {
			result.addAll(article.getTags());
		}

		return result;
	}

	/*
	 * flatmap 帮我把标签列表转为一个返回流，然后我们使用 collect 去创建一个集合作为返回值。
	 */
	public Set<String> getDistinctTagsStream(List<Article> articles) {
		return articles.stream()
				.flatMap(article -> article.getTags().stream())
				.collect(Collectors.toSet());
	}
}
