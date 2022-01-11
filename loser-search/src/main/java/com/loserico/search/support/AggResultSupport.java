package com.loserico.search.support;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.UnmappedTerms;
import org.elasticsearch.search.aggregations.metrics.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.InternalCardinality;
import org.elasticsearch.search.aggregations.metrics.InternalMax;
import org.elasticsearch.search.aggregations.metrics.InternalMin;
import org.elasticsearch.search.aggregations.metrics.InternalSum;
import org.elasticsearch.search.aggregations.metrics.InternalTopHits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Copyright: (C), 2021-06-21 21:51
 * <p>
 * <p>
 * Company: Sexy Uncle Inc.
 *
 * @author Rico Yu ricoyu520@gmail.com
 * @version 1.0
 */
@Slf4j
public final class AggResultSupport {
	
	public static Map<String, Object> compositeResult(Aggregations aggregations) {
		Map<String, Object> resultMap = new HashMap<>();
		
		if (aggregations == null) {
			return resultMap;
		}
		
		for (Aggregation aggregation : aggregations) {
			if (aggregation instanceof InternalMax) {
				Double value = ((InternalMax) aggregation).getValue();
				String name = aggregation.getName();
				resultMap.put(name, value);
			}
			
			if (aggregation instanceof InternalMin) {
				Double value = ((InternalMin) aggregation).getValue();
				String name = aggregation.getName();
				resultMap.put(name, value);
			}
			
			if (aggregation instanceof InternalAvg) {
				Double value = ((InternalAvg) aggregation).getValue();
				String name = aggregation.getName();
				resultMap.put(name, value);
			}
			
			if (aggregation instanceof LongTerms) {
				String name = aggregation.getName();
				List<LongTerms.Bucket> buckets = ((LongTerms) aggregation).getBuckets();
				Map<Object, Long> value = new HashMap<>(buckets.size());
				for (LongTerms.Bucket bucket : buckets) {
					Object key = bucket.getKey();
					long docCount = bucket.getDocCount();
					log.debug("Bucket: {}, Doc Count: {}", key, docCount);
					value.put(key, docCount);
				}
				resultMap.put(name, value);
			}
			
			if (aggregation instanceof StringTerms) {
				String name = aggregation.getName();
				List<StringTerms.Bucket> buckets = ((StringTerms) aggregation).getBuckets();
				Map<String, Long> value = new HashMap<>(buckets.size());
				for (StringTerms.Bucket bucket : buckets) {
					String key = bucket.getKeyAsString();
					long docCount = bucket.getDocCount();
					log.debug("Bucket: {}, Doc Count: {}", key, docCount);
					value.put(key, docCount);
				}
				resultMap.put(name, value);
			}
		}
		
		return resultMap;
	}
	
	public static <T> List<Map<String, T>> termsResult(Aggregations aggregations) {
		List<Map<String, T>> aggResults = new ArrayList<>();
		if (aggregations == null) {
			return aggResults;
		}
		for (Aggregation aggregation : aggregations) {
			if (aggregation instanceof UnmappedTerms) {
				log.warn("聚合的字段是一个未映射的字段, 比如enabled=false");
				continue;
			}
			//TODO 直接强转((StringTerms) aggregation)是有问题的, 有些可能是LongTerms等
			List<StringTerms.Bucket> buckets = ((StringTerms) aggregation).getBuckets();
			
			for (StringTerms.Bucket bucket : buckets) {
				Map<String, T> result = new HashMap<>();
				String key = bucket.getKeyAsString();
				Long docCount = bucket.getDocCount();
				log.debug("Bucket: {}, Doc Count: {}", key, docCount);
				result.put(key, (T) docCount);
				//没有子聚合的话subAggs也不会为null, 可以放心使用
				Aggregations subAggs = bucket.getAggregations();
				for (Aggregation subAgg : subAggs) {
					result.put(subAgg.getName(), aggResult(subAgg));
				}
				aggResults.add(result);
			}
		}
		
		return aggResults;
	}
	
	/**
	 * 返回terms聚合总的桶数量
	 * @param aggregations
	 * @return Integer
	 */
	public static Integer termsTotalBuckets(Aggregations aggregations) {
		if (aggregations == null) {
			return 0;
		}
		for (Aggregation aggregation : aggregations) {
			if (aggregation instanceof UnmappedTerms) {
				log.warn("聚合的字段是一个未映射的字段, 比如enabled=false");
				continue;
			}
			
			return ((StringTerms) aggregation).getBuckets().size();
		}
		
		return 0;
	}
	
	public static Map<String, Object> histogramResult(Aggregations aggregations) {
		Map<String, Object> aggResults = new HashMap<>();
		if (aggregations == null) {
			return aggResults;
		}
		for (Aggregation aggregation : aggregations) {
			List<InternalHistogram.Bucket> buckets = ((InternalHistogram) aggregation).getBuckets();
			Map<String, Object> result = new HashMap<>();
			for (InternalHistogram.Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				long docCount = bucket.getDocCount();
				log.debug("Bucket: {}, Doc Count: {}", key, docCount);
				result.put(key, docCount);
			}
			aggResults.put(aggregation.getName(), result);
		}
		
		return aggResults;
	}
	
	public static Map<String, Object> dateHistogramResult(Aggregations aggregations) {
		Map<String, Object> aggResults = new HashMap<>();
		if (aggregations == null) {
			return aggResults;
		}
		for (Aggregation aggregation : aggregations) {
			List<InternalDateHistogram.Bucket> buckets = ((InternalDateHistogram) aggregation).getBuckets();
			Map<String, Object> result = new HashMap<>();
			for (InternalDateHistogram.Bucket bucket : buckets) {
				String key = bucket.getKeyAsString();
				long docCount = bucket.getDocCount();
				log.debug("Bucket: {}, Doc Count: {}", key, docCount);
				result.put(key, docCount);
				
				Aggregations subAggs = bucket.getAggregations();
				for (Aggregation subAgg : subAggs) {
					result.put(subAgg.getName(), aggResult(subAgg));
				}
			}
			aggResults.put(aggregation.getName(), result);
		}
		
		return aggResults;
	}
	
	private static <T> T aggResult(Aggregation aggregation) {
		if (aggregation instanceof InternalHistogram) {
			return (T) aggResult((InternalHistogram) aggregation);
		}
		if (aggregation instanceof InternalDateHistogram) {
			return (T) aggResult((InternalDateHistogram) aggregation);
		}
		if (aggregation instanceof InternalAvg) {
			return (T) aggResult((InternalAvg) aggregation);
		}
		if (aggregation instanceof InternalTopHits) {
			return (T) aggResult((InternalTopHits) aggregation);
		}
		if (aggregation instanceof InternalSum) {
			return (T) aggResult((InternalSum) aggregation);
		}
		
		return null;
	}
	
	/**
	 * 负责处理Histogram结果
	 *
	 * @param aggregation
	 * @return Map<String, Object>
	 */
	private static Map<String, Object> aggResult(InternalHistogram aggregation) {
		List<InternalHistogram.Bucket> buckets = aggregation.getBuckets();
		Map<String, Object> result = new HashMap<>();
		for (InternalHistogram.Bucket bucket : buckets) {
			String key = bucket.getKeyAsString();
			long docCount = bucket.getDocCount();
			log.debug("Bucket: {}, Doc Count: {}", key, docCount);
			result.put(key, docCount);
		}
		return result;
	}
	
	/**
	 * 负责处理DateHistogram结果
	 *
	 * @param aggregation
	 * @return Map<String, Object>
	 */
	private static List<Map<String, Object>> aggResult(InternalDateHistogram aggregation) {
		List<InternalDateHistogram.Bucket> buckets = aggregation.getBuckets();
		List<Map<String, Object>> results = new ArrayList<>();
		for (InternalDateHistogram.Bucket bucket : buckets) {
			Map<String, Object> result = new HashMap<>();
			String key = bucket.getKeyAsString();
			long docCount = bucket.getDocCount();
			log.debug("Bucket: {}, Doc Count: {}", key, docCount);
			result.put(key, docCount);
			results.add(result);
			
			//如果有子聚合, 则嵌套处理子聚合
			Aggregations subAggs = bucket.getAggregations();
			for (Aggregation subAgg : subAggs) {
				result.put(subAgg.getName(), aggResult(subAgg));
			}
		}
		return results;
	}
	
	/**
	 * 负责处理AVG结果
	 *
	 * @param aggregation
	 * @return Double
	 */
	private static Double aggResult(InternalAvg aggregation) {
		return aggregation.getValue();
	}
	
	
	/**
	 * 负责处理Sum结果
	 *
	 * @param aggregation
	 * @return Map<String, Object>
	 */
	private static Double aggResult(InternalSum aggregation) {
		return aggregation.getValue();
	}
	
	/**
	 * 负责处理Top Hits聚合的结果
	 * @param aggregation
	 * @return
	 */
	private static List<Map<String, Object>> aggResult(InternalTopHits aggregation) {
		SearchHits hits = aggregation.getHits();
		return SearchHitsSupport.toList(hits.getHits(), Map.class);
	}
	
	public static Double avgResult(Aggregations aggregations) {
		if (aggregations == null) {
			return null;
		}
		for (Aggregation aggregation : aggregations) {
			return ((InternalAvg) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Long cardinalityResult(Aggregations aggregations) {
		if (aggregations == null) {
			return null;
		}
		for (Aggregation aggregation : aggregations) {
			return ((InternalCardinality) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Double maxResult(Aggregations aggregations) {
		if (aggregations == null) {
			return null;
		}
		for (Aggregation aggregation : aggregations) {
			return ((InternalMax) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Double minResult(Aggregations aggregations) {
		if (aggregations == null) {
			return null;
		}
		for (Aggregation aggregation : aggregations) {
			return ((InternalMin) aggregation).getValue();
		}
		
		return null;
	}
	
	public static Double sumResult(Aggregations aggregations) {
		if (aggregations == null) {
			return null;
		}
		for (Aggregation aggregation : aggregations) {
			return ((InternalSum) aggregation).getValue();
		}
		
		return null;
	}
}
