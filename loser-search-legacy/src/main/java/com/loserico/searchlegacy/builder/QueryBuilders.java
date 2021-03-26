package com.loserico.searchlegacy.builder;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.BoostingQueryBuilder;
import org.elasticsearch.index.query.CommonTermsQueryBuilder;
import org.elasticsearch.index.query.ConstantScoreQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.FieldMaskingSpanQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceRangeQueryBuilder;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.IndicesQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder.Item;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.MultiTermQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.index.query.SpanContainingQueryBuilder;
import org.elasticsearch.index.query.SpanFirstQueryBuilder;
import org.elasticsearch.index.query.SpanMultiTermQueryBuilder;
import org.elasticsearch.index.query.SpanNearQueryBuilder;
import org.elasticsearch.index.query.SpanNotQueryBuilder;
import org.elasticsearch.index.query.SpanOrQueryBuilder;
import org.elasticsearch.index.query.SpanQueryBuilder;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.elasticsearch.index.query.SpanWithinQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.TypeQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.indices.TermsLookup;
import org.elasticsearch.script.Script;

import java.util.Collection;

/**
 * A factory for simple "import static" usage.
 */
public class QueryBuilders {
	
	private ElasticQueryBuilder elasticQueryBuilder;
	
	public QueryBuilders(ElasticQueryBuilder elasticQueryBuilder) {
		this.elasticQueryBuilder = elasticQueryBuilder;
	}
	
	/**
	 * A query that matches on all documents.
	 */
	public ElasticQueryBuilder matchAllQuery() {
		elasticQueryBuilder.queryBuilder(new MatchAllQueryBuilder());
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a match query with type "BOOLEAN" for the provided field name and text.
	 *
	 * @param name The field name.
	 * @param text The query text (to be analyzed).
	 */
	public ElasticQueryBuilder matchQuery(String name, Object text) {
		elasticQueryBuilder.queryBuilder(new MatchQueryBuilder(name, text));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a common query for the provided field name and text.
	 *
	 * @param fieldName The field name.
	 * @param text      The query text (to be analyzed).
	 */
	public ElasticQueryBuilder commonTermsQuery(String fieldName, Object text) {
		elasticQueryBuilder.queryBuilder(new CommonTermsQueryBuilder(fieldName, text));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a match query with type "BOOLEAN" for the provided field name and text.
	 *
	 * @param fieldNames The field names.
	 * @param text       The query text (to be analyzed).
	 */
	public ElasticQueryBuilder multiMatchQuery(Object text, String... fieldNames) {
		elasticQueryBuilder.queryBuilder(new MultiMatchQueryBuilder(text, fieldNames));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a text query with type "PHRASE" for the provided field name and text.
	 *
	 * @param name The field name.
	 * @param text The query text (to be analyzed).
	 */
	public ElasticQueryBuilder matchPhraseQuery(String name, Object text) {
		elasticQueryBuilder.queryBuilder(new MatchPhraseQueryBuilder(name, text));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a match query with type "PHRASE_PREFIX" for the provided field name and text.
	 *
	 * @param name The field name.
	 * @param text The query text (to be analyzed).
	 */
	public ElasticQueryBuilder matchPhrasePrefixQuery(String name, Object text) {
		elasticQueryBuilder.queryBuilder(new MatchPhrasePrefixQueryBuilder(name, text));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that generates the union of documents produced by its sub-queries, and that scores each document
	 * with the maximum score for that document as produced by any sub-query, plus a tie breaking increment for any
	 * additional matching sub-queries.
	 */
	public ElasticQueryBuilder disMaxQuery() {
		elasticQueryBuilder.queryBuilder(new DisMaxQueryBuilder());
		return elasticQueryBuilder;
	}
	
	/**
	 * Constructs a query that will match only specific ids within all types.
	 */
	public ElasticQueryBuilder idsQuery() {
		elasticQueryBuilder.queryBuilder(new IdsQueryBuilder());
		return elasticQueryBuilder;
	}
	
	/**
	 * Constructs a query that will match only specific ids within types.
	 *
	 * @param types The mapping/doc type
	 */
	public ElasticQueryBuilder idsQuery(String... types) {
		elasticQueryBuilder.queryBuilder(new IdsQueryBuilder().types(types));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, String value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, int value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, long value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, float value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, double value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, boolean value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing a term.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 */
	public ElasticQueryBuilder termQuery(String name, Object value) {
		elasticQueryBuilder.queryBuilder(new TermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents using fuzzy query.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 * @see #matchQuery(String, Object)
	 * @see #rangeQuery(String)
	 */
	public ElasticQueryBuilder fuzzyQuery(String name, String value) {
		elasticQueryBuilder.queryBuilder(new FuzzyQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents using fuzzy query.
	 *
	 * @param name  The name of the field
	 * @param value The value of the term
	 * @see #matchQuery(String, Object)
	 * @see #rangeQuery(String)
	 */
	public ElasticQueryBuilder fuzzyQuery(String name, Object value) {
		elasticQueryBuilder.queryBuilder(new FuzzyQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents containing terms with a specified prefix.
	 *
	 * @param name   The name of the field
	 * @param prefix The prefix query
	 */
	public ElasticQueryBuilder prefixQuery(String name, String prefix) {
		elasticQueryBuilder.queryBuilder(new PrefixQueryBuilder(name, prefix));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents within an range of terms.
	 *
	 * @param name The field name
	 */
	public ElasticQueryBuilder rangeQuery(String name) {
		elasticQueryBuilder.queryBuilder(new RangeQueryBuilder(name));
		return elasticQueryBuilder;
	}
	
	/**
	 * Implements the wildcard search query. Supported wildcards are <tt>*</tt>, which
	 * matches any character sequence (including the empty one), and <tt>?</tt>,
	 * which matches any single character. Note this query can be slow, as it
	 * needs to iterate over many terms. In order to prevent extremely slow WildcardQueries,
	 * a Wildcard term should not start with one of the wildcards <tt>*</tt> or
	 * <tt>?</tt>.
	 *
	 * @param name  The field name
	 * @param query The wildcard query string
	 */
	public ElasticQueryBuilder wildcardQuery(String name, String query) {
		elasticQueryBuilder.queryBuilder(new WildcardQueryBuilder(name, query));
		return elasticQueryBuilder;
	}
	
	
	/**
	 * A Query that matches documents containing terms with a specified regular expression.
	 *
	 * @param name   The name of the field
	 * @param regexp The regular expression
	 */
	public ElasticQueryBuilder regexpQuery(String name, String regexp) {
		elasticQueryBuilder.queryBuilder(new RegexpQueryBuilder(name, regexp));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that parses a query string and runs it. There are two modes that this operates. The first,
	 * when no field is added (using {@link QueryStringQueryBuilder#field(String)}, will run the query once and non prefixed fields
	 * will use the {@link QueryStringQueryBuilder#defaultField(String)} set. The second, when one or more fields are added
	 * (using {@link QueryStringQueryBuilder#field(String)}), will run the parsed query against the provided fields, and combine
	 * them either using DisMax or a plain boolean query (see {@link QueryStringQueryBuilder#useDisMax(boolean)}).
	 *
	 * @param queryString The query string to run
	 */
	public ElasticQueryBuilder queryStringQuery(String queryString) {
		elasticQueryBuilder.queryBuilder(new QueryStringQueryBuilder(queryString));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that acts similar to a query_string query, but won't throw
	 * exceptions for any weird string syntax. See
	 * {@link org.apache.lucene.queryparser.simple.SimpleQueryParser} for the full
	 * supported syntax.
	 */
	public ElasticQueryBuilder simpleQueryStringQuery(String queryString) {
		elasticQueryBuilder.queryBuilder(new SimpleQueryStringBuilder(queryString));
		return elasticQueryBuilder;
	}
	
	/**
	 * The BoostingQuery class can be used to effectively demote results that match a given query.
	 * Unlike the "NOT" clause, this still selects documents that contain undesirable terms,
	 * but reduces their overall score:
	 */
	public ElasticQueryBuilder boostingQuery(QueryBuilder positiveQuery, QueryBuilder negativeQuery) {
		elasticQueryBuilder.queryBuilder(new BoostingQueryBuilder(positiveQuery, negativeQuery));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query that matches documents matching boolean combinations of other queries.
	 */
	public ElasticQueryBuilder boolQuery() {
		elasticQueryBuilder.queryBuilder(new BoolQueryBuilder());
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanTermQuery(String name, String value) {
		elasticQueryBuilder.queryBuilder(new SpanTermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanTermQuery(String name, int value) {
		elasticQueryBuilder.queryBuilder(new SpanTermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanTermQuery(String name, long value) {
		elasticQueryBuilder.queryBuilder(new SpanTermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanTermQuery(String name, float value) {
		elasticQueryBuilder.queryBuilder(new SpanTermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanTermQuery(String name, double value) {
		elasticQueryBuilder.queryBuilder(new SpanTermQueryBuilder(name, value));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanFirstQuery(SpanQueryBuilder match, int end) {
		elasticQueryBuilder.queryBuilder(new SpanFirstQueryBuilder(match, end));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanNearQuery(SpanQueryBuilder initialClause, int slop) {
		elasticQueryBuilder.queryBuilder(new SpanNearQueryBuilder(initialClause, slop));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanNotQuery(SpanQueryBuilder include, SpanQueryBuilder exclude) {
		elasticQueryBuilder.queryBuilder(new SpanNotQueryBuilder(include, exclude));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder spanOrQuery(SpanQueryBuilder initialClause) {
		elasticQueryBuilder.queryBuilder(new SpanOrQueryBuilder(initialClause));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a new {@code span_within} builder.
	 *
	 * @param big    the big clause, it must enclose {@code little} for a match.
	 * @param little the little clause, it must be contained within {@code big} for a match.
	 */
	public ElasticQueryBuilder spanWithinQuery(SpanQueryBuilder big, SpanQueryBuilder little) {
		elasticQueryBuilder.queryBuilder(new SpanWithinQueryBuilder(big, little));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a new {@code span_containing} builder.
	 *
	 * @param big    the big clause, it must enclose {@code little} for a match.
	 * @param little the little clause, it must be contained within {@code big} for a match.
	 */
	public ElasticQueryBuilder spanContainingQuery(SpanQueryBuilder big, SpanQueryBuilder little) {
		elasticQueryBuilder.queryBuilder(new SpanContainingQueryBuilder(big, little));
		return elasticQueryBuilder;
	}
	
	/**
	 * Creates a {@link SpanQueryBuilder} which allows having a sub query
	 * which implements {@link MultiTermQueryBuilder}. This is useful for
	 * having e.g. wildcard or fuzzy queries inside spans.
	 *
	 * @param multiTermQueryBuilder The {@link MultiTermQueryBuilder} that
	 *                              backs the created builder.
	 */
	
	public ElasticQueryBuilder spanMultiTermQueryBuilder(MultiTermQueryBuilder multiTermQueryBuilder) {
		elasticQueryBuilder.queryBuilder(new SpanMultiTermQueryBuilder(multiTermQueryBuilder));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder fieldMaskingSpanQuery(SpanQueryBuilder query, String field) {
		elasticQueryBuilder.queryBuilder(new FieldMaskingSpanQueryBuilder(query, field));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that wraps another query and simply returns a constant score equal to the
	 * query boost for every document in the query.
	 *
	 * @param queryBuilder The query to wrap in a constant score query
	 */
	public ElasticQueryBuilder constantScoreQuery(QueryBuilder queryBuilder) {
		elasticQueryBuilder.queryBuilder(new ConstantScoreQueryBuilder(queryBuilder));
		return elasticQueryBuilder;
	}
	
	/**
	 * A function_score query with no functions.
	 *
	 * @param queryBuilder The query to custom score
	 * @elasticQueryBuilder.queryBuilder(the function score query
	 */
	public ElasticQueryBuilder functionScoreQuery(QueryBuilder queryBuilder) {
		elasticQueryBuilder.queryBuilder(new FunctionScoreQueryBuilder(queryBuilder));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that allows to define a custom scoring function
	 *
	 * @param queryBuilder           The query to custom score
	 * @param filterFunctionBuilders the filters and functions to execute
	 * @elasticQueryBuilder.queryBuilder(the function score query
	 */
	public ElasticQueryBuilder functionScoreQuery(QueryBuilder queryBuilder, FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders) {
		elasticQueryBuilder.queryBuilder(new FunctionScoreQueryBuilder(queryBuilder, filterFunctionBuilders));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that allows to define a custom scoring function
	 *
	 * @param filterFunctionBuilders the filters and functions to execute
	 * @elasticQueryBuilder.queryBuilder(the function score query
	 */
	public ElasticQueryBuilder functionScoreQuery(FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders) {
		elasticQueryBuilder.queryBuilder(new FunctionScoreQueryBuilder(filterFunctionBuilders));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that allows to define a custom scoring function.
	 *
	 * @param function The function builder used to custom score
	 */
	public ElasticQueryBuilder functionScoreQuery(ScoreFunctionBuilder function) {
		elasticQueryBuilder.queryBuilder(new FunctionScoreQueryBuilder(function));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that allows to define a custom scoring function.
	 *
	 * @param queryBuilder The query to custom score
	 * @param function     The function builder used to custom score
	 */
	public ElasticQueryBuilder functionScoreQuery(QueryBuilder queryBuilder, ScoreFunctionBuilder function) {
		elasticQueryBuilder.queryBuilder((new FunctionScoreQueryBuilder(queryBuilder, function)));
		return elasticQueryBuilder;
	}
	
	/**
	 * A more like this query that finds documents that are "like" the provided texts or documents
	 * which is checked against the fields the query is constructed with.
	 *
	 * @param fields    the field names that will be used when generating the 'More Like This' query.
	 * @param likeTexts the text to use when generating the 'More Like This' query.
	 * @param likeItems the documents to use when generating the 'More Like This' query.
	 */
	public ElasticQueryBuilder moreLikeThisQuery(String[] fields, String[] likeTexts, Item[] likeItems) {
		elasticQueryBuilder.queryBuilder(new MoreLikeThisQueryBuilder(fields, likeTexts, likeItems));
		return elasticQueryBuilder;
	}
	
	/**
	 * A more like this query that finds documents that are "like" the provided texts or documents
	 * which is checked against the "_all" field.
	 *
	 * @param likeTexts the text to use when generating the 'More Like This' query.
	 * @param likeItems the documents to use when generating the 'More Like This' query.
	 */
	public ElasticQueryBuilder moreLikeThisQuery(String[] likeTexts, Item[] likeItems) {
		elasticQueryBuilder.queryBuilder(new MoreLikeThisQueryBuilder(null, likeTexts, likeItems));
		return elasticQueryBuilder;
	}
	
	/**
	 * A more like this query that finds documents that are "like" the provided texts
	 * which is checked against the "_all" field.
	 *
	 * @param likeTexts the text to use when generating the 'More Like This' query.
	 */
	public ElasticQueryBuilder moreLikeThisQuery(String[] likeTexts) {
		elasticQueryBuilder.queryBuilder(new MoreLikeThisQueryBuilder(null, likeTexts, null));
		return elasticQueryBuilder;
	}
	
	/**
	 * A more like this query that finds documents that are "like" the provided documents
	 * which is checked against the "_all" field.
	 *
	 * @param likeItems the documents to use when generating the 'More Like This' query.
	 */
	public ElasticQueryBuilder moreLikeThisQuery(Item[] likeItems) {
		elasticQueryBuilder.queryBuilder(new MoreLikeThisQueryBuilder(null, null, likeItems));
		return elasticQueryBuilder;
	}
	
	public ElasticQueryBuilder nestedQuery(String path, QueryBuilder query, ScoreMode scoreMode) {
		elasticQueryBuilder.queryBuilder(new NestedQueryBuilder(path, query, scoreMode));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, String... values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, int... values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, long... values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, float... values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, double... values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, Object... values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filer for a field based on several terms matching on any of them.
	 *
	 * @param name   The field name
	 * @param values The terms
	 */
	public ElasticQueryBuilder termsQuery(String name, Collection<?> values) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, values));
		return elasticQueryBuilder;
	}
	
	/**
	 * A query that will execute the wrapped query only for the specified
	 * indices, and "match_all" when it does not match those indices.
	 *
	 * @deprecated instead search on the `_index` field
	 */
	@Deprecated
	public ElasticQueryBuilder indicesQuery(QueryBuilder queryBuilder, String... indices) {
		// TODO remove this method in 6.0
		elasticQueryBuilder.queryBuilder(new IndicesQueryBuilder(queryBuilder, indices));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query builder which allows building a query thanks to a JSON string or binary data.
	 */
	public ElasticQueryBuilder wrapperQuery(String source) {
		elasticQueryBuilder.queryBuilder(new WrapperQueryBuilder(source));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query builder which allows building a query thanks to a JSON string or binary data.
	 */
	public ElasticQueryBuilder wrapperQuery(BytesReference source) {
		elasticQueryBuilder.queryBuilder(new WrapperQueryBuilder(source));
		return elasticQueryBuilder;
	}
	
	/**
	 * A Query builder which allows building a query thanks to a JSON string or binary data.
	 */
	public ElasticQueryBuilder wrapperQuery(byte[] source) {
		elasticQueryBuilder.queryBuilder(new WrapperQueryBuilder(source));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filter based on doc/mapping type.
	 */
	public ElasticQueryBuilder typeQuery(String type) {
		elasticQueryBuilder.queryBuilder(new TypeQueryBuilder(type));
		return elasticQueryBuilder;
	}
	
	/**
	 * A terms query that can extract the terms from another doc in an index.
	 */
	public ElasticQueryBuilder termsLookupQuery(String name, TermsLookup termsLookup) {
		elasticQueryBuilder.queryBuilder(new TermsQueryBuilder(name, termsLookup));
		return elasticQueryBuilder;
	}
	
	/**
	 * A builder for filter based on a script.
	 *
	 * @param script The script to filter by.
	 */
	public ElasticQueryBuilder scriptQuery(Script script) {
		elasticQueryBuilder.queryBuilder(new ScriptQueryBuilder(script));
		return elasticQueryBuilder;
	}
	
	
	/**
	 * A filter to filter based on a specific distance from a specific geo location / point.
	 *
	 * @param name The location field name.
	 */
	public ElasticQueryBuilder geoDistanceQuery(String name) {
		elasticQueryBuilder.queryBuilder(new GeoDistanceQueryBuilder(name));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filter to filter based on a specific range from a specific geo location / point.
	 *
	 * @param name  The location field name.
	 * @param point The point
	 */
	public ElasticQueryBuilder geoDistanceRangeQuery(String name, GeoPoint point) {
		elasticQueryBuilder.queryBuilder(new GeoDistanceRangeQueryBuilder(name, point));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filter to filter based on a specific range from a specific geo location / point.
	 *
	 * @param name    The location field name.
	 * @param geohash The point as geohash
	 */
	public ElasticQueryBuilder geoDistanceRangeQuery(String name, String geohash) {
		elasticQueryBuilder.queryBuilder(new GeoDistanceRangeQueryBuilder(name, geohash));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filter to filter based on a specific range from a specific geo location / point.
	 *
	 * @param name The location field name.
	 * @param lat  The points latitude
	 * @param lon  The points longitude
	 */
	public ElasticQueryBuilder geoDistanceRangeQuery(String name, double lat, double lon) {
		elasticQueryBuilder.queryBuilder(new GeoDistanceRangeQueryBuilder(name, lat, lon));
		return elasticQueryBuilder;
	}
	
	/**
	 * A filter to filter only documents where a field exists in them.
	 *
	 * @param name The name of the field
	 */
	public ElasticQueryBuilder existsQuery(String name) {
		elasticQueryBuilder.queryBuilder(new ExistsQueryBuilder(name));
		return elasticQueryBuilder;
	}
}
